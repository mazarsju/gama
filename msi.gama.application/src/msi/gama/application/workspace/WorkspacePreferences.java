package msi.gama.application.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IExportedPreferences;
import org.eclipse.core.runtime.preferences.IPreferenceFilter;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class WorkspacePreferences {

	static String lastWs;
	static String selectedWorkspaceRootLocation;
	static boolean applyPrefs;
	public static final String WS_IDENTIFIER = ".gama_application_workspace";
	public static final String MODEL_IDENTIFIER = WorkspaceModelsManager.getCurrentGamaStampString();

	public static String getSelectedWorkspaceRootLocation() {
		return selectedWorkspaceRootLocation;
	}

	public static void setSelectedWorkspaceRootLocation(final String s) {
		selectedWorkspaceRootLocation = s;
	}

	public static IPreferenceFilter[] getPreferenceFilters() {
		final IPreferenceFilter[] transfers = new IPreferenceFilter[1];

		// For export all create a preference filter that can export
		// all nodes of the Instance and Configuration scopes
		transfers[0] = new IPreferenceFilter() {

			@Override
			public String[] getScopes() {
				return new String[] { InstanceScope.SCOPE };
			}

			@Override
			@SuppressWarnings("rawtypes")
			public Map getMapping(final String scope) {
				return null;
			}
		};

		return transfers;
	}

	public static boolean applyPrefs() {
		return applyPrefs;
	}

	public static void setApplyPrefs(final boolean b) {
		applyPrefs = b;
	}

	public static void applyEclipsePreferences(final String targetDirectory) {
		final IPreferencesService service = Platform.getPreferencesService();
		IExportedPreferences prefs;

		try {
			final FileInputStream input = new FileInputStream(new File(targetDirectory + "/.gama.epf"));
			prefs = service.readPreferences(input);
			service.applyPreferences(prefs, WorkspacePreferences.getPreferenceFilters());
			input.close();
		} catch (final IOException e) {} catch (final CoreException e) {}
		WorkspacePreferences.setApplyPrefs(false);

	}

	/**
	 * Ensures a workspace directory is OK in regards of reading/writing, etc.
	 * This method will get called externally as well.
	 * 
	 * @param parentShell
	 *            Shell parent shell
	 * @param workspaceLocation
	 *            Directory the user wants to use
	 * @param askCreate
	 *            Whether to ask if to create the workspace or not in this
	 *            location if it does not exist already
	 * @param fromDialog
	 *            Whether this method was called from our dialog or from
	 *            somewhere else just to check a location
	 * @return null if everything is ok, or an error message if not
	 */
	public static String checkWorkspaceDirectory(final String workspaceLocation, final boolean askCreate,
		final boolean fromDialog, final boolean cloning) {
		final File f = new File(workspaceLocation);
		if ( !f.exists() ) {
			if ( askCreate ) {
				final boolean create =
					MessageDialog
						.openQuestion(Display.getDefault().getActiveShell(), "New Directory",
							workspaceLocation +
								" does not exist. Would you like to create a new workspace here" + (cloning
									? ", copy the projects and preferences of an existing workspace into it, " : "") +
								" and restart the application ?");
				if ( create ) {
					try {
						f.mkdirs();
						final File wsDot = new File(workspaceLocation + File.separator + WS_IDENTIFIER);
						wsDot.createNewFile();
						final File dotFile = new File(workspaceLocation + File.separator + MODEL_IDENTIFIER);
						dotFile.createNewFile();
					} catch (final RuntimeException err) {
						err.printStackTrace();
						return "Error creating directories, please check folder permissions";
					} catch (final IOException er) {
						er.printStackTrace();
						return "Error creating directories, please check folder permissions";
					}
				}

				if ( !f.exists() ) {
					return "The selected directory does not exist";
				} else {
					return null;
				}
			}
		}

		if ( !f.canRead() ) {
			// scope.getGui().debug("The selected directory is not readable");
			return "The selected directory is not readable";
		}

		if ( !f.isDirectory() ) {
			// scope.getGui().debug("The selected path is not a directory");
			return "The selected path is not a directory";
		}

		final File wsTest = new File(workspaceLocation + File.separator + WS_IDENTIFIER);
		if ( fromDialog ) {
			if ( !wsTest.exists() ) {
				final boolean create = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "New Workspace",
					"The directory '" + wsTest.getAbsolutePath() +
						"' exists but is not identified as a GAMA workspace. \n\nWould you like to use it anyway ?");
				if ( create ) {
					try {
						f.mkdirs();
						final File wsDot = new File(workspaceLocation + File.separator + WS_IDENTIFIER);
						wsDot.createNewFile();
					} catch (final Exception err) {
						return "Error creating directories, please check folder permissions";
					}
				} else {
					return "Please select a directory for your workspace";
				}

				if ( !wsTest.exists() ) { return "The selected directory does not exist"; }

				return null;
			}
		} else {
			if ( !wsTest.exists() ) { return "The selected directory is not a workspace directory"; }
		}
		final File dotFile = new File(workspaceLocation + File.separator + MODEL_IDENTIFIER);
		if ( !dotFile.exists() ) {
			if ( fromDialog ) {
				final boolean create = MessageDialog.openConfirm(Display.getDefault().getActiveShell(),
					"Outdated version of the models library",
					"The workspace contains an old version of the models library. Do you want to proceed anyway ?");
				if ( create ) {
					try {
						dotFile.createNewFile();
					} catch (final IOException e) {
						return "Error updating the models library";
					}
					return null;
				}
			}

			return "models";
		} else if ( cloning ) {
			final boolean b = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Existing workspace",
				"The path entered is a path to an existing workspace. All its contents will be erased and replaced by the current workspace contents. Proceed anyway ?");
			if ( !b ) { return ""; }
		}
		return null;
	}

}
