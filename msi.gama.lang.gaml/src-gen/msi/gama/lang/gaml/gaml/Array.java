/**
 */
package msi.gama.lang.gaml.gaml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Array</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link msi.gama.lang.gaml.gaml.Array#getExprs <em>Exprs</em>}</li>
 * </ul>
 *
 * @see msi.gama.lang.gaml.gaml.GamlPackage#getArray()
 * @model
 * @generated
 */
public interface Array extends Expression
{
  /**
   * Returns the value of the '<em><b>Exprs</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Exprs</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Exprs</em>' containment reference.
   * @see #setExprs(ExpressionList)
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getArray_Exprs()
   * @model containment="true"
   * @generated
   */
  ExpressionList getExprs();

  /**
   * Sets the value of the '{@link msi.gama.lang.gaml.gaml.Array#getExprs <em>Exprs</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Exprs</em>' containment reference.
   * @see #getExprs()
   * @generated
   */
  void setExprs(ExpressionList value);

} // Array
