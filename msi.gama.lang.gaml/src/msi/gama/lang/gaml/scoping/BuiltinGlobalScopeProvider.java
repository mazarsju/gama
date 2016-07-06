/*********************************************************************************************
 *
 *
 * 'BuiltinGlobalScopeProvider.java', in plugin 'msi.gama.lang.gaml', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 *
 *
 **********************************************************************************************/
// (c) Vincent Simonet, 2011
package msi.gama.lang.gaml.scoping;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.diagnostics.Diagnostic;
import org.eclipse.xtext.diagnostics.Severity;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.scoping.IGlobalScopeProvider;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.ImportUriGlobalScopeProvider;
import org.eclipse.xtext.util.IResourceScopeCache;
import org.eclipse.xtext.validation.EObjectDiagnosticImpl;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.Provider;

import gnu.trove.map.hash.THashMap;
import gnu.trove.procedure.TObjectObjectProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.hash.THashSet;
import msi.gama.lang.gaml.gaml.GamlDefinition;
import msi.gama.lang.gaml.gaml.GamlPackage;
import msi.gama.lang.gaml.resource.GamlResource;
import msi.gama.lang.utils.EGaml;
import msi.gama.runtime.GAMA;
import msi.gama.util.GamaPair;
import msi.gaml.compilation.AbstractGamlAdditions;
import msi.gaml.descriptions.IDescription;
import msi.gaml.descriptions.IGamlDescription;
import msi.gaml.descriptions.ModelDescription;
import msi.gaml.descriptions.OperatorProto;
import msi.gaml.expressions.IExpressionCompiler;
import msi.gaml.expressions.IExpressionFactory;
import msi.gaml.factories.DescriptionFactory;
import msi.gaml.types.IType;
import msi.gaml.types.Signature;
import msi.gaml.types.Types;

/**
 * Global GAML scope provider supporting built-in definitions.
 * <p>
 * This global provider generates a global scope which consists in:
 * </p>
 * <ul>
 * <li>Built-in definitions which are defined in the diffents plug-in bundles
 * providing contributions to GAML,</li>
 * <li>A global scope, which is computed by a ImportURI global scope provider.
 * </li>
 * </ul>
 *
 * @author Vincent Simonet, adapted for GAML by Alexis Drogoul, 2012
 */
public class BuiltinGlobalScopeProvider implements IGlobalScopeProvider {

	@Inject
	private ImportUriGlobalScopeProvider uriScopeProvider;
	// @Inject
	// private ResourceSetGlobalScopeProvider resourceSetScopeProvider;

	static final HashMap EMPTY_MAP = new HashMap();
	static final LinkedHashSet EMPTY_SET = new LinkedHashSet();
	private static HashMap<EClass, Resource> resources;
	private static HashMap<EClass, HashMap<QualifiedName, IEObjectDescription>> descriptions = null;
	private static EClass eType, eVar, eSkill, eAction, eUnit, eEquation;

	static XtextResourceSet rs = new XtextResourceSet();

	public static class AllImportUriGlobalScopeProvider extends ImportUriGlobalScopeProvider {

		@Inject
		private IResourceScopeCache cache;

		@Override
		protected LinkedHashSet<URI> getImportedUris(final Resource resource) {
			return cache.get(AllImportUriGlobalScopeProvider.class.getName(), resource,
					new Provider<LinkedHashSet<URI>>() {
						@Override
						public LinkedHashSet<URI> get() {
							// System.out.println("Getting imported URIS from "
							// + resource);
							final Set<URI> uris = ((GamlResource) resource)
									.computeAllImportedURIs(resource.getResourceSet()).keySet();
							if (uris.size() <= 1)
								return EMPTY_SET;
							uris.remove(resource.getURI());
							return new LinkedHashSet(uris);
						}
					});

		}
	}

	public static class ImmutableMap implements Map<String, String> {

		private final String[] contents;

		public ImmutableMap(final String... strings) {
			contents = strings == null ? new String[0] : strings;
		}

		/**
		 * Method size()
		 * 
		 * @see java.util.Map#size()
		 */
		@Override
		public int size() {
			return contents.length;
		}

		/**
		 * Method isEmpty()
		 * 
		 * @see java.util.Map#isEmpty()
		 */
		@Override
		public boolean isEmpty() {
			return contents.length == 0;
		}

		/**
		 * Method containsKey()
		 * 
		 * @see java.util.Map#containsKey(java.lang.Object)
		 */
		@Override
		public boolean containsKey(final Object key) {
			for (int i = 0; i < contents.length; i += 2) {
				final String k = contents[i];
				if (k.equals(key)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Method containsValue()
		 * 
		 * @see java.util.Map#containsValue(java.lang.Object)
		 */
		@Override
		public boolean containsValue(final Object value) {
			for (int i = 1; i < contents.length; i += 2) {
				final String k = contents[i];
				if (k.equals(value)) {
					return true;
				}
			}
			return false;

		}

		/**
		 * Method get()
		 * 
		 * @see java.util.Map#get(java.lang.Object)
		 */
		@Override
		public String get(final Object key) {
			for (int i = 0; i < contents.length; i += 2) {
				final String k = contents[i];
				if (k.equals(key)) {
					return contents[i + 1];
				}
			}
			return null;

		}

		/**
		 * Method put()
		 * 
		 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
		 */
		@Override
		public String put(final String key, final String value) {
			// Only replace
			for (int i = 0; i < contents.length; i += 2) {
				final String k = contents[i];
				if (k.equals(key)) {
					final String oldValue = contents[i + 1];
					contents[i + 1] = value;
					return oldValue;
				}
			}

			return null;

		}

		/**
		 * Method remove()
		 * 
		 * @see java.util.Map#remove(java.lang.Object)
		 */
		@Override
		public String remove(final Object key) {
			// No remove
			return null;
		}

		/**
		 * Method putAll()
		 * 
		 * @see java.util.Map#putAll(java.util.Map)
		 */
		@Override
		public void putAll(final Map<? extends String, ? extends String> m) {
			for (final Map.Entry<? extends String, ? extends String> entry : m.entrySet()) {
				put(entry.getKey(), entry.getValue());
			}
		}

		/**
		 * Method clear()
		 * 
		 * @see java.util.Map#clear()
		 */
		@Override
		public void clear() {
		}

		/**
		 * Method keySet()
		 * 
		 * @see java.util.Map#keySet()
		 */
		@Override
		public Set<String> keySet() {
			final THashSet<String> keys = new THashSet();
			for (int i = 0; i < contents.length; i += 2) {
				keys.add(contents[i]);
			}
			return keys;
		}

		/**
		 * Method values()
		 * 
		 * @see java.util.Map#values()
		 */
		@Override
		public Collection<String> values() {
			final THashSet<String> values = new THashSet();
			for (int i = 1; i < contents.length; i += 2) {
				values.add(contents[i]);
			}
			return values;
		}

		/**
		 * Method entrySet()
		 * 
		 * @see java.util.Map#entrySet()
		 */
		@Override
		public Set<java.util.Map.Entry<String, String>> entrySet() {
			final THashSet<Map.Entry<String, String>> keys = new THashSet();
			for (int i = 0; i < contents.length; i += 2) {
				final Map.Entry<String, String> entry = new GamaPair<>(contents[i], contents[i + 1], Types.STRING,
						Types.STRING);
				keys.add(entry);
			}
			return keys;
		}

	}

	static Resource createResource(final String uri) {
		Resource r = rs.getResource(URI.createURI(uri, false), false);
		if (r == null) {
			r = rs.createResource(URI.createURI(uri, false));
		}
		DescriptionFactory.documentResource(r);
		return r;
	}

	static void initResources() {
		eType = GamlPackage.eINSTANCE.getTypeDefinition();
		eVar = GamlPackage.eINSTANCE.getVarDefinition();
		eSkill = GamlPackage.eINSTANCE.getSkillFakeDefinition();
		eAction = GamlPackage.eINSTANCE.getActionDefinition();
		eUnit = GamlPackage.eINSTANCE.getUnitFakeDefinition();
		eEquation = GamlPackage.eINSTANCE.getEquationDefinition();
		resources = new HashMap();
		resources.put(eType, createResource("types.xmi"));
		resources.put(eVar, createResource("vars.xmi"));
		resources.put(eSkill, createResource("skills.xmi"));
		resources.put(eUnit, createResource("units.xmi"));
		resources.put(eAction, createResource("actions.xmi"));
		resources.put(eEquation, createResource("equations.xmi"));
		descriptions = new HashMap();
		descriptions.put(eVar, new HashMap());
		descriptions.put(eType, new HashMap());
		descriptions.put(eSkill, new HashMap());
		descriptions.put(eUnit, new HashMap());
		descriptions.put(eAction, new HashMap());
		descriptions.put(eEquation, new HashMap());
	}

	public static boolean contains(final QualifiedName name) {
		for (final Map<QualifiedName, IEObjectDescription> map : descriptions.values()) {
			if (map.containsKey(name)) {
				return true;
			}
		}
		return false;
	}

	static GamlDefinition add(final EClass eClass, final String t) {
		final GamlDefinition stub = (GamlDefinition) EGaml.getFactory().create(eClass);
		stub.setName(t);
		resources.get(eClass).getContents().add(stub);
		final IEObjectDescription e = EObjectDescription.create(t,
				stub/* , userData */);
		descriptions.get(eClass).put(e.getName(), e);
		return stub;
	}

	static void add(final EClass eClass, final String t, final OperatorProto o) {
		final GamlDefinition stub = (GamlDefinition) EGaml.getFactory().create(eClass);
		stub.setName(t);
		Map<String, String> doc;
		resources.get(eClass).getContents().add(stub);
		final IGamlDescription d = GAMA.isInHeadLessMode() ? null : DescriptionFactory.getGamlDocumentation(o);

		if (d != null) {
			doc = new ImmutableMap("doc", d.getDocumentation(), "title", d.getTitle(), "type", "operator");
		} else {
			doc = new ImmutableMap("type", "operator");
		}
		final IEObjectDescription e = EObjectDescription.create(t, stub, doc);
		descriptions.get(eClass).put(e.getName(), e);

	}

	static void addVar(final EClass eClass, final String t, final IGamlDescription o, final String keyword) {

		final GamlDefinition stub = (GamlDefinition) EGaml.getFactory().create(eClass);
		// TODO Add the fields definition here
		stub.setName(t);
		resources.get(eClass).getContents().add(stub);
		final IGamlDescription d = GAMA.isInHeadLessMode() ? null : DescriptionFactory.getGamlDocumentation(o);
		// IGamlDescription d = null;
		Map<String, String> doc;
		if (d != null) {
			doc = new ImmutableMap("doc", d.getDocumentation(), "title", d.getTitle(), "type", keyword);
		} else {
			doc = new ImmutableMap("type", keyword);
		}
		final IEObjectDescription e = EObjectDescription.create(t, stub, doc);
		descriptions.get(eClass).put(e.getName(), e);

	}

	static void addAction(final EClass eClass, final String t, final IGamlDescription o) {
		final GamlDefinition stub = (GamlDefinition) EGaml.getFactory().create(eClass);
		// TODO Add the fields definition here
		stub.setName(t);
		resources.get(eClass).getContents().add(stub);
		final IGamlDescription d = GAMA.isInHeadLessMode() ? null : DescriptionFactory.getGamlDocumentation(o);
		DescriptionFactory.setGamlDocumentationOfBuiltIn(stub, o);
		Map<String, String> doc;
		if (d != null) {
			doc = new ImmutableMap("doc", d.getDocumentation(), "title", d.getTitle(), "type", "action");
		} else {
			doc = new ImmutableMap("type", "action");
		}
		final IEObjectDescription e = EObjectDescription.create(t, stub, doc);
		descriptions.get(eClass).put(e.getName(), e);

	}

	static void addUnit(final EClass eClass, final String t) {
		final GamlDefinition stub = (GamlDefinition) EGaml.getFactory().create(eClass);
		stub.setName(t);
		resources.get(eClass).getContents().add(stub);
		final String d = IExpressionFactory.UNITS_EXPR.get(t).getDocumentation();
		final Map<String, String> doc = new ImmutableMap("title", d, "type", "unit");
		final IEObjectDescription e = EObjectDescription.create(t, stub, doc);
		descriptions.get(eClass).put(e.getName(), e);

	}

	static void addType(final EClass eClass, final String t, final IType type) {
		final GamlDefinition stub = (GamlDefinition) EGaml.getFactory().create(eClass);
		// TODO Add the fields definition here
		stub.setName(t);
		resources.get(eClass).getContents().add(stub);
		final Map<String, String> doc = new ImmutableMap("title", "Type " + type, "type", "type");
		final IEObjectDescription e = EObjectDescription.create(t, stub, doc);
		descriptions.get(eClass).put(e.getName(), e);

	}

	/**
	 * Get the object descriptions for the built-in types.
	 */
	public static HashMap<QualifiedName, IEObjectDescription> getEObjectDescriptions(final EClass eClass) {
		createDescriptions();
		return descriptions.get(eClass);
	}

	public static void createDescriptions() {
		if (descriptions == null) {
			initResources();
			for (final String t : Types.getTypeNames()) {
				addType(eType, t, Types.get(t));
				add(eVar, t);
				add(eAction, t);
			}
			for (final String t : AbstractGamlAdditions.CONSTANTS) {
				add(eType, t);
				add(eVar, t);
			}
			for (final String t : IExpressionFactory.UNITS_EXPR.keySet()) {
				addUnit(eUnit, t);
			}
			for (final OperatorProto t : AbstractGamlAdditions.getAllFields()) {
				addVar(eVar, t.getName(), t, "field");
			}
			for (final IDescription t : AbstractGamlAdditions.getAllVars()) {
				addVar(eVar, t.getName(), t, "variable");
			}
			for (final String t : AbstractGamlAdditions.getAllSkills()) {
				add(eSkill, t);
				add(eVar, t);
			}
			for (final IDescription t : AbstractGamlAdditions.getAllActions()) {
				addAction(eAction, t.getName(), t);

				final GamlDefinition def = add(eVar, t.getName());
				DescriptionFactory.setGamlDocumentation(def, t);
			}
			final OperatorProto[] p = new OperatorProto[1];
			IExpressionCompiler.OPERATORS
					.forEachEntry(new TObjectObjectProcedure<String, THashMap<Signature, OperatorProto>>() {

						@Override
						public boolean execute(final String a, final THashMap<Signature, OperatorProto> b) {
							p[0] = null;
							b.forEachValue(new TObjectProcedure<OperatorProto>() {

								@Override
								public boolean execute(final OperatorProto object) {
									p[0] = object;
									return false;
								}
							});
							add(eAction, a, p[0]);
							return true;
						}
					});

		}
	}

	static {
		// AD 15/01/16: added to make sure that the XText builder can wait
		// until, at least, the main artefacts of GAMA have been built.
		while (ModelDescription.ROOT == null) {
			try {
				Thread.sleep(100);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		createDescriptions();
	}

	/**
	 * Implementation of IGlobalScopeProvider.
	 */
	@Override
	public IScope getScope(final Resource context, final EReference reference,
			final Predicate<IEObjectDescription> filter) {
		final EClass eclass = reference.getEReferenceType();
		HashMap<QualifiedName, IEObjectDescription> descriptions = getEObjectDescriptions(eclass);
		if (descriptions == null) {
			descriptions = EMPTY_MAP;
		}
		IScope parent;
		try {
			parent = uriScopeProvider.getScope(context, reference, filter);
			// parent = resourceSetScopeProvider.getScope(context, reference,
			// filter);
		} catch (final IllegalStateException e) {
			e.printStackTrace();
			final Diagnostic d = new EObjectDiagnosticImpl(Severity.ERROR, "",
					"The imports of this model are not valid", context.getContents().get(0), null, 0, null);
			context.getErrors().add(d);
			return IScope.NULLSCOPE;
		}

		return new MapBasedScope(parent, descriptions);
	}
}
