/**
 */
package msi.gama.lang.gaml.gaml.util;

import msi.gama.lang.gaml.gaml.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see msi.gama.lang.gaml.gaml.GamlPackage
 * @generated
 */
public class GamlAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static GamlPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GamlAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = GamlPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GamlSwitch<Adapter> modelSwitch =
    new GamlSwitch<Adapter>()
    {
      @Override
      public Adapter caseEntry(Entry object)
      {
        return createEntryAdapter();
      }
      @Override
      public Adapter caseStringEvaluator(StringEvaluator object)
      {
        return createStringEvaluatorAdapter();
      }
      @Override
      public Adapter caseModel(Model object)
      {
        return createModelAdapter();
      }
      @Override
      public Adapter caseBlock(Block object)
      {
        return createBlockAdapter();
      }
      @Override
      public Adapter caseImport(Import object)
      {
        return createImportAdapter();
      }
      @Override
      public Adapter casePragma(Pragma object)
      {
        return createPragmaAdapter();
      }
      @Override
      public Adapter caseStatement(Statement object)
      {
        return createStatementAdapter();
      }
      @Override
      public Adapter caseS_Global(S_Global object)
      {
        return createS_GlobalAdapter();
      }
      @Override
      public Adapter caseS_Entities(S_Entities object)
      {
        return createS_EntitiesAdapter();
      }
      @Override
      public Adapter caseS_Environment(S_Environment object)
      {
        return createS_EnvironmentAdapter();
      }
      @Override
      public Adapter caseS_Species(S_Species object)
      {
        return createS_SpeciesAdapter();
      }
      @Override
      public Adapter caseS_Experiment(S_Experiment object)
      {
        return createS_ExperimentAdapter();
      }
      @Override
      public Adapter caseS_Do(S_Do object)
      {
        return createS_DoAdapter();
      }
      @Override
      public Adapter caseS_Loop(S_Loop object)
      {
        return createS_LoopAdapter();
      }
      @Override
      public Adapter caseS_If(S_If object)
      {
        return createS_IfAdapter();
      }
      @Override
      public Adapter caseS_Other(S_Other object)
      {
        return createS_OtherAdapter();
      }
      @Override
      public Adapter caseS_Return(S_Return object)
      {
        return createS_ReturnAdapter();
      }
      @Override
      public Adapter caseS_Declaration(S_Declaration object)
      {
        return createS_DeclarationAdapter();
      }
      @Override
      public Adapter caseS_Reflex(S_Reflex object)
      {
        return createS_ReflexAdapter();
      }
      @Override
      public Adapter caseS_Definition(S_Definition object)
      {
        return createS_DefinitionAdapter();
      }
      @Override
      public Adapter caseS_Assignment(S_Assignment object)
      {
        return createS_AssignmentAdapter();
      }
      @Override
      public Adapter caseS_DirectAssignment(S_DirectAssignment object)
      {
        return createS_DirectAssignmentAdapter();
      }
      @Override
      public Adapter caseS_Set(S_Set object)
      {
        return createS_SetAdapter();
      }
      @Override
      public Adapter caseS_Equations(S_Equations object)
      {
        return createS_EquationsAdapter();
      }
      @Override
      public Adapter caseS_Solve(S_Solve object)
      {
        return createS_SolveAdapter();
      }
      @Override
      public Adapter caseS_Display(S_Display object)
      {
        return createS_DisplayAdapter();
      }
      @Override
      public Adapter casespeciesOrGridDisplayStatement(speciesOrGridDisplayStatement object)
      {
        return createspeciesOrGridDisplayStatementAdapter();
      }
      @Override
      public Adapter caseParameters(Parameters object)
      {
        return createParametersAdapter();
      }
      @Override
      public Adapter caseActionArguments(ActionArguments object)
      {
        return createActionArgumentsAdapter();
      }
      @Override
      public Adapter caseArgumentDefinition(ArgumentDefinition object)
      {
        return createArgumentDefinitionAdapter();
      }
      @Override
      public Adapter caseFacet(Facet object)
      {
        return createFacetAdapter();
      }
      @Override
      public Adapter caseExpression(Expression object)
      {
        return createExpressionAdapter();
      }
      @Override
      public Adapter caseArgumentPair(ArgumentPair object)
      {
        return createArgumentPairAdapter();
      }
      @Override
      public Adapter caseFunction(Function object)
      {
        return createFunctionAdapter();
      }
      @Override
      public Adapter caseExpressionList(ExpressionList object)
      {
        return createExpressionListAdapter();
      }
      @Override
      public Adapter caseVariableRef(VariableRef object)
      {
        return createVariableRefAdapter();
      }
      @Override
      public Adapter caseTypeInfo(TypeInfo object)
      {
        return createTypeInfoAdapter();
      }
      @Override
      public Adapter caseGamlDefinition(GamlDefinition object)
      {
        return createGamlDefinitionAdapter();
      }
      @Override
      public Adapter caseEquationDefinition(EquationDefinition object)
      {
        return createEquationDefinitionAdapter();
      }
      @Override
      public Adapter caseTypeDefinition(TypeDefinition object)
      {
        return createTypeDefinitionAdapter();
      }
      @Override
      public Adapter caseVarDefinition(VarDefinition object)
      {
        return createVarDefinitionAdapter();
      }
      @Override
      public Adapter caseActionDefinition(ActionDefinition object)
      {
        return createActionDefinitionAdapter();
      }
      @Override
      public Adapter caseUnitFakeDefinition(UnitFakeDefinition object)
      {
        return createUnitFakeDefinitionAdapter();
      }
      @Override
      public Adapter caseTypeFakeDefinition(TypeFakeDefinition object)
      {
        return createTypeFakeDefinitionAdapter();
      }
      @Override
      public Adapter caseActionFakeDefinition(ActionFakeDefinition object)
      {
        return createActionFakeDefinitionAdapter();
      }
      @Override
      public Adapter caseSkillFakeDefinition(SkillFakeDefinition object)
      {
        return createSkillFakeDefinitionAdapter();
      }
      @Override
      public Adapter caseVarFakeDefinition(VarFakeDefinition object)
      {
        return createVarFakeDefinitionAdapter();
      }
      @Override
      public Adapter caseEquationFakeDefinition(EquationFakeDefinition object)
      {
        return createEquationFakeDefinitionAdapter();
      }
      @Override
      public Adapter caseTerminalExpression(TerminalExpression object)
      {
        return createTerminalExpressionAdapter();
      }
      @Override
      public Adapter caseS_Action(S_Action object)
      {
        return createS_ActionAdapter();
      }
      @Override
      public Adapter caseS_Var(S_Var object)
      {
        return createS_VarAdapter();
      }
      @Override
      public Adapter casePair(Pair object)
      {
        return createPairAdapter();
      }
      @Override
      public Adapter caseIf(If object)
      {
        return createIfAdapter();
      }
      @Override
      public Adapter caseCast(Cast object)
      {
        return createCastAdapter();
      }
      @Override
      public Adapter caseBinary(Binary object)
      {
        return createBinaryAdapter();
      }
      @Override
      public Adapter caseUnit(Unit object)
      {
        return createUnitAdapter();
      }
      @Override
      public Adapter caseUnary(Unary object)
      {
        return createUnaryAdapter();
      }
      @Override
      public Adapter caseAccess(Access object)
      {
        return createAccessAdapter();
      }
      @Override
      public Adapter caseArray(Array object)
      {
        return createArrayAdapter();
      }
      @Override
      public Adapter casePoint(Point object)
      {
        return createPointAdapter();
      }
      @Override
      public Adapter caseParameter(Parameter object)
      {
        return createParameterAdapter();
      }
      @Override
      public Adapter caseUnitName(UnitName object)
      {
        return createUnitNameAdapter();
      }
      @Override
      public Adapter caseTypeRef(TypeRef object)
      {
        return createTypeRefAdapter();
      }
      @Override
      public Adapter caseSkillRef(SkillRef object)
      {
        return createSkillRefAdapter();
      }
      @Override
      public Adapter caseActionRef(ActionRef object)
      {
        return createActionRefAdapter();
      }
      @Override
      public Adapter caseEquationRef(EquationRef object)
      {
        return createEquationRefAdapter();
      }
      @Override
      public Adapter caseIntLiteral(IntLiteral object)
      {
        return createIntLiteralAdapter();
      }
      @Override
      public Adapter caseDoubleLiteral(DoubleLiteral object)
      {
        return createDoubleLiteralAdapter();
      }
      @Override
      public Adapter caseColorLiteral(ColorLiteral object)
      {
        return createColorLiteralAdapter();
      }
      @Override
      public Adapter caseStringLiteral(StringLiteral object)
      {
        return createStringLiteralAdapter();
      }
      @Override
      public Adapter caseBooleanLiteral(BooleanLiteral object)
      {
        return createBooleanLiteralAdapter();
      }
      @Override
      public Adapter caseReservedLiteral(ReservedLiteral object)
      {
        return createReservedLiteralAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Entry <em>Entry</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Entry
   * @generated
   */
  public Adapter createEntryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.StringEvaluator <em>String Evaluator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.StringEvaluator
   * @generated
   */
  public Adapter createStringEvaluatorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Model
   * @generated
   */
  public Adapter createModelAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Block <em>Block</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Block
   * @generated
   */
  public Adapter createBlockAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Import <em>Import</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Import
   * @generated
   */
  public Adapter createImportAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Pragma <em>Pragma</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Pragma
   * @generated
   */
  public Adapter createPragmaAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Statement <em>Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Statement
   * @generated
   */
  public Adapter createStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Global <em>SGlobal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Global
   * @generated
   */
  public Adapter createS_GlobalAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Entities <em>SEntities</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Entities
   * @generated
   */
  public Adapter createS_EntitiesAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Environment <em>SEnvironment</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Environment
   * @generated
   */
  public Adapter createS_EnvironmentAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Species <em>SSpecies</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Species
   * @generated
   */
  public Adapter createS_SpeciesAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Experiment <em>SExperiment</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Experiment
   * @generated
   */
  public Adapter createS_ExperimentAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Do <em>SDo</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Do
   * @generated
   */
  public Adapter createS_DoAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Loop <em>SLoop</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Loop
   * @generated
   */
  public Adapter createS_LoopAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_If <em>SIf</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_If
   * @generated
   */
  public Adapter createS_IfAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Other <em>SOther</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Other
   * @generated
   */
  public Adapter createS_OtherAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Return <em>SReturn</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Return
   * @generated
   */
  public Adapter createS_ReturnAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Declaration <em>SDeclaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Declaration
   * @generated
   */
  public Adapter createS_DeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Reflex <em>SReflex</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Reflex
   * @generated
   */
  public Adapter createS_ReflexAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Definition <em>SDefinition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Definition
   * @generated
   */
  public Adapter createS_DefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Assignment <em>SAssignment</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Assignment
   * @generated
   */
  public Adapter createS_AssignmentAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_DirectAssignment <em>SDirect Assignment</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_DirectAssignment
   * @generated
   */
  public Adapter createS_DirectAssignmentAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Set <em>SSet</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Set
   * @generated
   */
  public Adapter createS_SetAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Equations <em>SEquations</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Equations
   * @generated
   */
  public Adapter createS_EquationsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Solve <em>SSolve</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Solve
   * @generated
   */
  public Adapter createS_SolveAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Display <em>SDisplay</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Display
   * @generated
   */
  public Adapter createS_DisplayAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.speciesOrGridDisplayStatement <em>species Or Grid Display Statement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.speciesOrGridDisplayStatement
   * @generated
   */
  public Adapter createspeciesOrGridDisplayStatementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Parameters <em>Parameters</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Parameters
   * @generated
   */
  public Adapter createParametersAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.ActionArguments <em>Action Arguments</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.ActionArguments
   * @generated
   */
  public Adapter createActionArgumentsAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.ArgumentDefinition <em>Argument Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.ArgumentDefinition
   * @generated
   */
  public Adapter createArgumentDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Facet <em>Facet</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Facet
   * @generated
   */
  public Adapter createFacetAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Expression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Expression
   * @generated
   */
  public Adapter createExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.ArgumentPair <em>Argument Pair</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.ArgumentPair
   * @generated
   */
  public Adapter createArgumentPairAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Function <em>Function</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Function
   * @generated
   */
  public Adapter createFunctionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.ExpressionList <em>Expression List</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.ExpressionList
   * @generated
   */
  public Adapter createExpressionListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.VariableRef <em>Variable Ref</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.VariableRef
   * @generated
   */
  public Adapter createVariableRefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.TypeInfo <em>Type Info</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.TypeInfo
   * @generated
   */
  public Adapter createTypeInfoAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.GamlDefinition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.GamlDefinition
   * @generated
   */
  public Adapter createGamlDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.EquationDefinition <em>Equation Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.EquationDefinition
   * @generated
   */
  public Adapter createEquationDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.TypeDefinition <em>Type Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.TypeDefinition
   * @generated
   */
  public Adapter createTypeDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.VarDefinition <em>Var Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.VarDefinition
   * @generated
   */
  public Adapter createVarDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.ActionDefinition <em>Action Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.ActionDefinition
   * @generated
   */
  public Adapter createActionDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.UnitFakeDefinition <em>Unit Fake Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.UnitFakeDefinition
   * @generated
   */
  public Adapter createUnitFakeDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.TypeFakeDefinition <em>Type Fake Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.TypeFakeDefinition
   * @generated
   */
  public Adapter createTypeFakeDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.ActionFakeDefinition <em>Action Fake Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.ActionFakeDefinition
   * @generated
   */
  public Adapter createActionFakeDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.SkillFakeDefinition <em>Skill Fake Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.SkillFakeDefinition
   * @generated
   */
  public Adapter createSkillFakeDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.VarFakeDefinition <em>Var Fake Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.VarFakeDefinition
   * @generated
   */
  public Adapter createVarFakeDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.EquationFakeDefinition <em>Equation Fake Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.EquationFakeDefinition
   * @generated
   */
  public Adapter createEquationFakeDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.TerminalExpression <em>Terminal Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.TerminalExpression
   * @generated
   */
  public Adapter createTerminalExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Action <em>SAction</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Action
   * @generated
   */
  public Adapter createS_ActionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.S_Var <em>SVar</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.S_Var
   * @generated
   */
  public Adapter createS_VarAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Pair <em>Pair</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Pair
   * @generated
   */
  public Adapter createPairAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.If <em>If</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.If
   * @generated
   */
  public Adapter createIfAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Cast <em>Cast</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Cast
   * @generated
   */
  public Adapter createCastAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Binary <em>Binary</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Binary
   * @generated
   */
  public Adapter createBinaryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Unit <em>Unit</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Unit
   * @generated
   */
  public Adapter createUnitAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Unary <em>Unary</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Unary
   * @generated
   */
  public Adapter createUnaryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Access <em>Access</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Access
   * @generated
   */
  public Adapter createAccessAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Array <em>Array</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Array
   * @generated
   */
  public Adapter createArrayAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Point <em>Point</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Point
   * @generated
   */
  public Adapter createPointAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.Parameter <em>Parameter</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.Parameter
   * @generated
   */
  public Adapter createParameterAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.UnitName <em>Unit Name</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.UnitName
   * @generated
   */
  public Adapter createUnitNameAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.TypeRef <em>Type Ref</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.TypeRef
   * @generated
   */
  public Adapter createTypeRefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.SkillRef <em>Skill Ref</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.SkillRef
   * @generated
   */
  public Adapter createSkillRefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.ActionRef <em>Action Ref</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.ActionRef
   * @generated
   */
  public Adapter createActionRefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.EquationRef <em>Equation Ref</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.EquationRef
   * @generated
   */
  public Adapter createEquationRefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.IntLiteral <em>Int Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.IntLiteral
   * @generated
   */
  public Adapter createIntLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.DoubleLiteral <em>Double Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.DoubleLiteral
   * @generated
   */
  public Adapter createDoubleLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.ColorLiteral <em>Color Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.ColorLiteral
   * @generated
   */
  public Adapter createColorLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.StringLiteral <em>String Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.StringLiteral
   * @generated
   */
  public Adapter createStringLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.BooleanLiteral <em>Boolean Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.BooleanLiteral
   * @generated
   */
  public Adapter createBooleanLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link msi.gama.lang.gaml.gaml.ReservedLiteral <em>Reserved Literal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see msi.gama.lang.gaml.gaml.ReservedLiteral
   * @generated
   */
  public Adapter createReservedLiteralAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //GamlAdapterFactory
