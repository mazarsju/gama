/**
 */
package msi.gama.lang.gaml.gaml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>If</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link msi.gama.lang.gaml.gaml.If#getIfFalse <em>If False</em>}</li>
 * </ul>
 *
 * @see msi.gama.lang.gaml.gaml.GamlPackage#getIf()
 * @model
 * @generated
 */
public interface If extends Expression
{
  /**
   * Returns the value of the '<em><b>If False</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>If False</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>If False</em>' containment reference.
   * @see #setIfFalse(Expression)
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getIf_IfFalse()
   * @model containment="true"
   * @generated
   */
  Expression getIfFalse();

  /**
   * Sets the value of the '{@link msi.gama.lang.gaml.gaml.If#getIfFalse <em>If False</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>If False</em>' containment reference.
   * @see #getIfFalse()
   * @generated
   */
  void setIfFalse(Expression value);

} // If
