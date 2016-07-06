/**
 */
package msi.gama.lang.gaml.gaml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>SDefinition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link msi.gama.lang.gaml.gaml.S_Definition#getTkey <em>Tkey</em>}</li>
 *   <li>{@link msi.gama.lang.gaml.gaml.S_Definition#getArgs <em>Args</em>}</li>
 * </ul>
 *
 * @see msi.gama.lang.gaml.gaml.GamlPackage#getS_Definition()
 * @model
 * @generated
 */
public interface S_Definition extends S_Declaration, ActionDefinition
{
  /**
   * Returns the value of the '<em><b>Tkey</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Tkey</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Tkey</em>' containment reference.
   * @see #setTkey(Expression)
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getS_Definition_Tkey()
   * @model containment="true"
   * @generated
   */
  Expression getTkey();

  /**
   * Sets the value of the '{@link msi.gama.lang.gaml.gaml.S_Definition#getTkey <em>Tkey</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Tkey</em>' containment reference.
   * @see #getTkey()
   * @generated
   */
  void setTkey(Expression value);

  /**
   * Returns the value of the '<em><b>Args</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Args</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Args</em>' containment reference.
   * @see #setArgs(ActionArguments)
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getS_Definition_Args()
   * @model containment="true"
   * @generated
   */
  ActionArguments getArgs();

  /**
   * Sets the value of the '{@link msi.gama.lang.gaml.gaml.S_Definition#getArgs <em>Args</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Args</em>' containment reference.
   * @see #getArgs()
   * @generated
   */
  void setArgs(ActionArguments value);

} // S_Definition
