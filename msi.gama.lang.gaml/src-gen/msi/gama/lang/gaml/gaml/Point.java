/**
 */
package msi.gama.lang.gaml.gaml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Point</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link msi.gama.lang.gaml.gaml.Point#getZ <em>Z</em>}</li>
 * </ul>
 *
 * @see msi.gama.lang.gaml.gaml.GamlPackage#getPoint()
 * @model
 * @generated
 */
public interface Point extends Expression
{
  /**
   * Returns the value of the '<em><b>Z</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Z</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Z</em>' containment reference.
   * @see #setZ(Expression)
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getPoint_Z()
   * @model containment="true"
   * @generated
   */
  Expression getZ();

  /**
   * Sets the value of the '{@link msi.gama.lang.gaml.gaml.Point#getZ <em>Z</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Z</em>' containment reference.
   * @see #getZ()
   * @generated
   */
  void setZ(Expression value);

} // Point
