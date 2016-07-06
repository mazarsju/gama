/**
 * Created by drogoul, 10 mars 2015
 *
 */
package msi.gama.outputs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import msi.gama.common.GamaPreferences;
import msi.gama.common.GamaPreferences.IPreferenceChangeListener;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.metamodel.shape.ILocation;
import msi.gama.util.GamaColor;

/**
 */
public class LayeredDisplayData {

	public enum Changes {
		SPLIT_LAYER, CHANGE_CAMERA, THREED_VIEW, CAMERA_POS, BACKGROUND, HIGHLIGHT, ZOOM;
	}

	public static final String JAVA2D = "java2D";
	public static final String OPENGL = "opengl";
	public static final String WEB = "web";
	public static final String THREED = "3D";

	public static interface DisplayDataListener {

		void changed(Changes property, boolean value);
	}

	final Set<DisplayDataListener> listeners = new HashSet();

	public void addListener(final DisplayDataListener listener) {
		listeners.add(listener);
	}

	public void removeListener(final DisplayDataListener listener) {
		listeners.remove(listener);
	}

	public void notifyListeners(final Changes property, final boolean value) {
		for (final DisplayDataListener listener : listeners) {
			listener.changed(property, value);
		}
	}

	/**
	 * Colors
	 */
	private Color backgroundColor = GamaPreferences.CORE_BACKGROUND.getValue();
	private Color ambientColor = new GamaColor(127, 127, 127, 255); // default
																	// value
	private Color highlightColor = GamaPreferences.CORE_HIGHLIGHT.getValue();

	/**
	 * Properties
	 */
	private boolean isAutosaving = false;
	private boolean isSynchronized = GamaPreferences.CORE_SYNC.getValue();
	private String displayType = GamaPreferences.CORE_DISPLAY.getValue().equalsIgnoreCase(JAVA2D) ? JAVA2D : OPENGL;
	private double envWidth = 0d;
	private double envHeight = 0d;
	private boolean isAntialiasing = GamaPreferences.CORE_ANTIALIAS.getValue();
	private ILocation imageDimension = new GamaPoint(-1, -1);
	private Double zoomLevel = null;
	private final LightPropertiesStructure lights[] = new LightPropertiesStructure[8];

	/**
	 * OpenGL
	 */

	public static ILocation noChange = new GamaPoint(-1, -1, -1);
	//
	private boolean isOutputtingIn3D = false;
	private boolean isTesselating = false;
	private boolean isTriangulating = false;
	// private int traceSize = 0;
	private boolean isZFighting = true; // GamaPreferences.CORE_Z_FIGHTING.getValue();
	private boolean isDrawingNormals = false; // GamaPreferences.CORE_DRAW_NORM.getValue();
	public boolean isComputingNormals = true;
	private boolean isDisplayingAsACube = false; // GamaPreferences.CORE_CUBEDISPLAY.getValue();
	private boolean ortho = false;
	private boolean disableCameraInteraction = false; // "fixed_camera" facet of
														// the display
	private boolean isShowingFPS = false; // GamaPreferences.CORE_SHOW_FPS.getValue();
	private boolean isDrawingEnvironment = GamaPreferences.CORE_DRAW_ENV.getValue();
	private boolean isLightOn = true; // GamaPreferences.CORE_IS_LIGHT_ON.getValue();
	private ILocation cameraPos = getNoChange();
	private ILocation cameraLookPos = getNoChange();
	private ILocation cameraUpVector = new GamaPoint(0, 1, 0);
	private int cameraLens = 45;
	private boolean isDrawingPolygons = true;
	private boolean isRotating;
	private boolean isUsingArcBallCamera = true;
	private boolean isSplittingLayers;
	private volatile boolean isCameraLock = true;

	/**
	 * Overlay
	 */

	private boolean isDisplayingScale = GamaPreferences.CORE_SCALE.getValue();
	private boolean isFullScreen = false;

	/**
	 *
	 */

	IPreferenceChangeListener highlightListener = new IPreferenceChangeListener<Color>() {

		@Override
		public boolean beforeValueChange(final Color newValue) {
			return true;
		}

		@Override
		public void afterValueChange(final Color newValue) {
			setHighlightColor(newValue);

		}
	};

	public LayeredDisplayData() {
		GamaPreferences.CORE_HIGHLIGHT.addChangeListener(highlightListener);
	}

	public void dispose() {
		GamaPreferences.CORE_HIGHLIGHT.removeChangeListener(highlightListener);
	}

	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor
	 *            the backgroundColor to set
	 */
	public void setBackgroundColor(final Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		notifyListeners(Changes.BACKGROUND, true);
	}

	/**
	 * @return the autosave
	 */
	public boolean isAutosave() {
		return isAutosaving;
	}

	/**
	 * @param autosave
	 *            the autosave to set
	 */
	public void setAutosave(final boolean autosave) {
		this.isAutosaving = autosave;
	}

	public boolean isTriangulation() {
		return isTriangulating;
	}

	public void setTriangulation(final boolean t) {
		isTriangulating = t;
	}

	/**
	 * @return the output3D
	 */
	public boolean isOutput3D() {
		return isOutputtingIn3D;
	}

	/**
	 * @param output3d
	 *            the output3D to set
	 */
	public void setOutput3D(final boolean output3d) {
		isOutputtingIn3D = output3d;
		notifyListeners(LayeredDisplayData.Changes.THREED_VIEW, output3d);
	}

	/**
	 * @return the tesselation
	 */
	public boolean isTesselation() {
		return isTesselating;
	}

	/**
	 * @param tesselation
	 *            the tesselation to set
	 */
	public void setTesselation(final boolean tesselation) {
		this.isTesselating = tesselation;
	}

	/**
	 * @return the z_fighting
	 */
	public boolean isZ_fighting() {
		return isZFighting;
	}

	/**
	 * @param z_fighting
	 *            the z_fighting to set
	 */
	public void setZ_fighting(final boolean z_fighting) {
		this.isZFighting = z_fighting;
	}

	/**
	 * @return the draw_norm
	 */
	public boolean isDraw_norm() {
		return isDrawingNormals;
	}

	/**
	 * @param draw_norm
	 *            the draw_norm to set
	 */
	public void setDraw_norm(final boolean draw_norm) {
		this.isDrawingNormals = draw_norm;
	}

	/**
	 * @return the cubeDisplay
	 */
	public boolean isCubeDisplay() {
		return isDisplayingAsACube;
	}

	/**
	 * @param cubeDisplay
	 *            the cubeDisplay to set
	 */
	public void setCubeDisplay(final boolean cubeDisplay) {
		this.isDisplayingAsACube = cubeDisplay;
	}

	/**
	 * @return the ortho
	 */
	public boolean isOrtho() {
		return ortho;
	}

	/**
	 * @param ortho
	 *            the ortho to set
	 */
	public void setOrtho(final boolean ortho) {
		this.ortho = ortho;
	}

	/**
	 * @return the displayScale
	 */
	public boolean isDisplayScale() {
		return isDisplayingScale;
	}

	/**
	 * @param displayScale
	 *            the displayScale to set
	 */
	public void setDisplayScale(final boolean displayScale) {
		this.isDisplayingScale = displayScale;
	}

	/**
	 * @return the showfps
	 */
	public boolean isShowfps() {
		return isShowingFPS;
	}

	/**
	 * @param showfps
	 *            the showfps to set
	 */
	public void setShowfps(final boolean showfps) {
		this.isShowingFPS = showfps;
	}

	/**
	 * @return the drawEnv
	 */
	public boolean isDrawEnv() {
		return isDrawingEnvironment;
	}

	/**
	 * @param drawEnv
	 *            the drawEnv to set
	 */
	public void setDrawEnv(final boolean drawEnv) {
		this.isDrawingEnvironment = drawEnv;
	}

	/**
	 * @return the isLightOn
	 */
	public boolean isLightOn() {
		return isLightOn;
	}

	/**
	 * @param isLightOn
	 *            the isLightOn to set
	 */
	public void setLightOn(final boolean isLightOn) {
		this.isLightOn = isLightOn;
	}

	public List<LightPropertiesStructure> getDiffuseLights() {
		final ArrayList<LightPropertiesStructure> result = new ArrayList<LightPropertiesStructure>();
		for (final LightPropertiesStructure lightProp : lights) {
			if (lightProp != null) {
				// TODO : check if the light is active
				result.add(lightProp);
			}
		}
		return result;
	}

	public void setLightActive(final int lightId, final boolean value) {
		if (lights[lightId] == null) {
			lights[lightId] = new LightPropertiesStructure();
		}
		lights[lightId].id = lightId;
		lights[lightId].active = value;
	}

	public void setLightType(final int lightId, final String type) {
		if (type.compareTo("direction") == 0) {
			lights[lightId].type = LightPropertiesStructure.TYPE.DIRECTION;
		} else if (type.compareTo("point") == 0) {
			lights[lightId].type = LightPropertiesStructure.TYPE.POINT;
		} else {
			lights[lightId].type = LightPropertiesStructure.TYPE.SPOT;
		}
	}

	public void setLightPosition(final int lightId, final GamaPoint position) {
		lights[lightId].position = position;
	}

	public void setLightDirection(final int lightId, final GamaPoint direction) {
		lights[lightId].direction = direction;
	}

	public void setDiffuseLightColor(final int lightId, final GamaColor color) {
		lights[lightId].color = color;
	}

	public void setSpotAngle(final int lightId, final float angle) {
		lights[lightId].spotAngle = angle;
	}

	public void setLinearAttenuation(final int lightId, final float linearAttenuation) {
		lights[lightId].linearAttenuation = linearAttenuation;
	}

	public void setQuadraticAttenuation(final int lightId, final float quadraticAttenuation) {
		lights[lightId].quadraticAttenuation = quadraticAttenuation;
	}

	public void setDrawLight(final int lightId, final boolean value) {
		lights[lightId].drawLight = value;
	}

	public void disableCameraInteractions(final boolean disableCamInteract) {
		this.disableCameraInteraction = disableCamInteract;
	}

	public boolean cameraInteractionDisabled() {
		return disableCameraInteraction;
	}

	/**
	 * @return the ambientLightColor
	 */
	public Color getAmbientLightColor() {
		return ambientColor;
	}

	/**
	 * @param ambientLightColor
	 *            the ambientLightColor to set
	 */
	public void setAmbientLightColor(final Color ambientLightColor) {
		this.ambientColor = ambientLightColor;
	}

	/**
	 * @return the cameraPos
	 */
	public ILocation getCameraPos() {
		return cameraPos;
	}

	/**
	 * @param cameraPos
	 *            the cameraPos to set
	 */
	public void setCameraPos(final ILocation cameraPos) {
		if (!this.cameraPos.equals(cameraPos)) {
			this.cameraPos = cameraPos == null ? getNoChange() : cameraPos;
			notifyListeners(Changes.CAMERA_POS, true);
		}
	}

	/**
	 * @return the cameraLookPos
	 */
	public ILocation getCameraLookPos() {
		return cameraLookPos;
	}

	/**
	 * @param cameraLookPos
	 *            the cameraLookPos to set
	 */
	public void setCameraLookPos(final ILocation cameraLookPos) {
		if (!this.cameraLookPos.equals(cameraLookPos)) {
			this.cameraLookPos = cameraLookPos == null ? getNoChange() : cameraLookPos;
			notifyListeners(Changes.CAMERA_POS, true);
		}
	}

	/**
	 * @return the cameraUpVector
	 */
	public ILocation getCameraUpVector() {
		return cameraUpVector;
	}

	/**
	 * @param cameraUpVector
	 *            the cameraUpVector to set
	 */
	public void setCameraUpVector(final ILocation cameraUpVector) {
		if (!this.cameraUpVector.equals(cameraUpVector)) {
			this.cameraUpVector = cameraUpVector == null ? new GamaPoint(0, 1, 0) : cameraUpVector;
			notifyListeners(Changes.CAMERA_POS, true);
		}
	}

	/**
	 * @return the cameraLens
	 */
	public int getCameralens() {
		return cameraLens;
	}

	/**
	 * @param cameraLens
	 *            the cameraLens to set
	 */
	public void setCameraLens(final int cameraLens) {
		if (this.cameraLens != cameraLens) {
			this.cameraLens = cameraLens;
		}
	}

	/**
	 * @return the polygonMode
	 */
	public boolean isPolygonMode() {
		return isDrawingPolygons;
	}

	/**
	 * @param polygonMode
	 *            the polygonMode to set
	 */
	public void setPolygonMode(final boolean polygonMode) {
		this.isDrawingPolygons = polygonMode;
	}

	/**
	 * @return the displayType
	 */
	public String getDisplayType() {
		return displayType;
	}

	/**
	 * @param displayType
	 *            the displayType to set
	 */
	public void setDisplayType(final String displayType) {
		this.displayType = displayType;
	}

	/**
	 * @return the imageDimension
	 */
	public ILocation getImageDimension() {
		return imageDimension;
	}

	/**
	 * @param imageDimension
	 *            the imageDimension to set
	 */
	public void setImageDimension(final ILocation imageDimension) {
		this.imageDimension = imageDimension;
	}

	/**
	 * @return the envWidth
	 */
	public double getEnvWidth() {
		return envWidth;
	}

	/**
	 * @param envWidth
	 *            the envWidth to set
	 */
	public void setEnvWidth(final double envWidth) {
		this.envWidth = envWidth;
	}

	/**
	 * @return the envHeight
	 */
	public double getEnvHeight() {
		return envHeight;
	}

	/**
	 * @param envHeight
	 *            the envHeight to set
	 */
	public void setEnvHeight(final double envHeight) {
		this.envHeight = envHeight;
	}

	/**
	 * @return
	 */
	public Color getHighlightColor() {
		return highlightColor;
	}

	public void setHighlightColor(final Color hc) {
		highlightColor = hc;
		notifyListeners(Changes.HIGHLIGHT, true);
	}

	public boolean isAntialias() {
		return isAntialiasing;
	}

	public void setAntialias(final boolean a) {
		isAntialiasing = a;
	}

	/**
	 * @return
	 */
	public boolean isRotationOn() {
		return isRotating;
	}

	public void setRotation(final boolean r) {
		isRotating = r;
	}

	/**
	 * @return
	 */
	public boolean isArcBallCamera() {
		return isUsingArcBallCamera;
	}

	public void setArcBallCamera(final boolean c) {
		isUsingArcBallCamera = c;
		notifyListeners(Changes.CHANGE_CAMERA, c);
	}

	/**
	 * @return
	 */
	public boolean isLayerSplitted() {
		return isSplittingLayers;
	}

	public void setLayerSplitted(final boolean s) {
		isSplittingLayers = s;
		notifyListeners(Changes.SPLIT_LAYER, s);
	}

	/**
	 * @return
	 */
	public boolean isCameraLock() {
		return isCameraLock;
	}

	public void setCameraLock(final boolean s) {
		isCameraLock = s;
	}

	public static ILocation getNoChange() {
		return noChange;
	}

	public boolean isSynchronized() {
		return isSynchronized;
	}

	public void setSynchronized(final boolean isSynchronized) {
		this.isSynchronized = isSynchronized;
	}

	/**
	 * @return the zoomLevel
	 */
	public Double getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * @param zoomLevel
	 *            the zoomLevel to set
	 */
	public void setZoomLevel(final Double zoomLevel) {
		if (this.zoomLevel != null && this.zoomLevel.equals(zoomLevel)) {
			return;
		}
		this.zoomLevel = zoomLevel;
		notifyListeners(Changes.ZOOM, true);
	}

	public boolean isFullScreen() {
		return isFullScreen;
	}

	public void setFullScreen(final boolean fs) {
		isFullScreen = fs;
	}

}