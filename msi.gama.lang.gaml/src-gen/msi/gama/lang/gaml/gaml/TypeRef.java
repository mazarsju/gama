/**
 */
package msi.gama.lang.gaml.gaml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type Ref</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link msi.gama.lang.gaml.gaml.TypeRef#getRef <em>Ref</em>}</li>
 *   <li>{@link msi.gama.lang.gaml.gaml.TypeRef#getParameter <em>Parameter</em>}</li>
 * </ul>
 *
 * @see msi.gama.lang.gaml.gaml.GamlPackage#getTypeRef()
 * @model
 * @generated
 */
public interface TypeRef extends Expression
{
  /**
   * Returns the value of the '<em><b>Ref</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ref</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ref</em>' reference.
   * @see #setRef(TypeDefinition)
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getTypeRef_Ref()
   * @model
   * @generated
   */
  TypeDefinition getRef();

  /**
   * Sets the value of the '{@link msi.gama.lang.gaml.gaml.TypeRef#getRef <em>Ref</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Ref</em>' reference.
   * @see #getRef()
   * @generated
   */
  void setRef(TypeDefinition value);

  /**
   * Returns the value of the '<em><b>Parameter</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parameter</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameter</em>' containment reference.
   * @see #setParameter(TypeInfo)
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getTypeRef_Parameter()
   * @model containment="true"
   * @generated
   */
  TypeInfo getParameter();

  /**
   * Sets the value of the '{@link msi.gama.lang.gaml.gaml.TypeRef#getParameter <em>Parameter</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parameter</em>' containment reference.
   * @see #getParameter()
   * @generated
   */
  void setParameter(TypeInfo value);

} // TypeRef
