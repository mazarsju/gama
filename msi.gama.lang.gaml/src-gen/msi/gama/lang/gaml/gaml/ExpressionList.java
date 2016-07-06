/**
 */
package msi.gama.lang.gaml.gaml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link msi.gama.lang.gaml.gaml.ExpressionList#getExprs <em>Exprs</em>}</li>
 * </ul>
 *
 * @see msi.gama.lang.gaml.gaml.GamlPackage#getExpressionList()
 * @model
 * @generated
 */
public interface ExpressionList extends Expression
{
  /**
   * Returns the value of the '<em><b>Exprs</b></em>' containment reference list.
   * The list contents are of type {@link msi.gama.lang.gaml.gaml.Expression}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Exprs</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Exprs</em>' containment reference list.
   * @see msi.gama.lang.gaml.gaml.GamlPackage#getExpressionList_Exprs()
   * @model containment="true"
   * @generated
   */
  EList<Expression> getExprs();

} // ExpressionList
