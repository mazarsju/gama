/**
 */
package msi.gama.lang.gaml.gaml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>String Evaluator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link msi.gama.lang.gaml.gaml.StringEvaluator#getToto <em>Toto</em>}</li>
 *   <li>{@link msi.gama.lang.gaml.gaml.StringEvaluator#getExpr <em>Expr</em>}</li>
 * </ul>
 *
 * @see msi.gama.lang.gaml.gaml.GamlPackage#getStringEvaluator()
 * @model
 * @generated
 */
public interface StringEvaluator extends Entry
{
  /**
   * Returns the value of the '<em><b>Toto</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Toto</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Toto</em>' attribute.
   * @see #setToto(String)
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getStringEvaluator_Toto()
   * @model
   * @generated
   */
  String getToto();

  /**
   * Sets the value of the '{@link msi.gama.lang.gaml.gaml.StringEvaluator#getToto <em>Toto</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Toto</em>' attribute.
   * @see #getToto()
   * @generated
   */
  void setToto(String value);

  /**
   * Returns the value of the '<em><b>Expr</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Expr</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expr</em>' containment reference.
   * @see #setExpr(Expression)
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getStringEvaluator_Expr()
   * @model containment="true"
   * @generated
   */
  Expression getExpr();

  /**
   * Sets the value of the '{@link msi.gama.lang.gaml.gaml.StringEvaluator#getExpr <em>Expr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expr</em>' containment reference.
   * @see #getExpr()
   * @generated
   */
  void setExpr(Expression value);

} // StringEvaluator
