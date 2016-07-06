/**
 */
package msi.gama.lang.gaml.gaml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link msi.gama.lang.gaml.gaml.Model#getPragmas <em>Pragmas</em>}</li>
 *   <li>{@link msi.gama.lang.gaml.gaml.Model#getImports <em>Imports</em>}</li>
 *   <li>{@link msi.gama.lang.gaml.gaml.Model#getBlock <em>Block</em>}</li>
 * </ul>
 *
 * @see msi.gama.lang.gaml.gaml.GamlPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends Entry, VarDefinition
{
  /**
   * Returns the value of the '<em><b>Pragmas</b></em>' containment reference list.
   * The list contents are of type {@link msi.gama.lang.gaml.gaml.Pragma}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pragmas</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pragmas</em>' containment reference list.
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getModel_Pragmas()
   * @model containment="true"
   * @generated
   */
  EList<Pragma> getPragmas();

  /**
   * Returns the value of the '<em><b>Imports</b></em>' containment reference list.
   * The list contents are of type {@link msi.gama.lang.gaml.gaml.Import}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Imports</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Imports</em>' containment reference list.
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getModel_Imports()
   * @model containment="true"
   * @generated
   */
  EList<Import> getImports();

  /**
   * Returns the value of the '<em><b>Block</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Block</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Block</em>' containment reference.
   * @see #setBlock(Block)
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getModel_Block()
   * @model containment="true"
   * @generated
   */
  Block getBlock();

  /**
   * Sets the value of the '{@link msi.gama.lang.gaml.gaml.Model#getBlock <em>Block</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Block</em>' containment reference.
   * @see #getBlock()
   * @generated
   */
  void setBlock(Block value);

} // Model
