/**
 */
package msi.gama.lang.gaml.gaml.impl;

import java.util.Collection;

import msi.gama.lang.gaml.gaml.Block;
import msi.gama.lang.gaml.gaml.Expression;
import msi.gama.lang.gaml.gaml.Facet;
import msi.gama.lang.gaml.gaml.GamlPackage;
import msi.gama.lang.gaml.gaml.Statement;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link msi.gama.lang.gaml.gaml.impl.StatementImpl#getKey <em>Key</em>}</li>
 *   <li>{@link msi.gama.lang.gaml.gaml.impl.StatementImpl#getFirstFacet <em>First Facet</em>}</li>
 *   <li>{@link msi.gama.lang.gaml.gaml.impl.StatementImpl#getExpr <em>Expr</em>}</li>
 *   <li>{@link msi.gama.lang.gaml.gaml.impl.StatementImpl#getFacets <em>Facets</em>}</li>
 *   <li>{@link msi.gama.lang.gaml.gaml.impl.StatementImpl#getBlock <em>Block</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StatementImpl extends MinimalEObjectImpl.Container implements Statement
{
  /**
   * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKey()
   * @generated
   * @ordered
   */
  protected static final String KEY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKey()
   * @generated
   * @ordered
   */
  protected String key = KEY_EDEFAULT;

  /**
   * The default value of the '{@link #getFirstFacet() <em>First Facet</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFirstFacet()
   * @generated
   * @ordered
   */
  protected static final String FIRST_FACET_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFirstFacet() <em>First Facet</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFirstFacet()
   * @generated
   * @ordered
   */
  protected String firstFacet = FIRST_FACET_EDEFAULT;

  /**
   * The cached value of the '{@link #getExpr() <em>Expr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpr()
   * @generated
   * @ordered
   */
  protected Expression expr;

  /**
   * The cached value of the '{@link #getFacets() <em>Facets</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFacets()
   * @generated
   * @ordered
   */
  protected EList<Facet> facets;

  /**
   * The cached value of the '{@link #getBlock() <em>Block</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBlock()
   * @generated
   * @ordered
   */
  protected Block block;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected StatementImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return GamlPackage.Literals.STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getKey()
  {
    return key;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setKey(String newKey)
  {
    String oldKey = key;
    key = newKey;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GamlPackage.STATEMENT__KEY, oldKey, key));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getFirstFacet()
  {
    return firstFacet;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFirstFacet(String newFirstFacet)
  {
    String oldFirstFacet = firstFacet;
    firstFacet = newFirstFacet;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GamlPackage.STATEMENT__FIRST_FACET, oldFirstFacet, firstFacet));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Expression getExpr()
  {
    return expr;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetExpr(Expression newExpr, NotificationChain msgs)
  {
    Expression oldExpr = expr;
    expr = newExpr;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GamlPackage.STATEMENT__EXPR, oldExpr, newExpr);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExpr(Expression newExpr)
  {
    if (newExpr != expr)
    {
      NotificationChain msgs = null;
      if (expr != null)
        msgs = ((InternalEObject)expr).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GamlPackage.STATEMENT__EXPR, null, msgs);
      if (newExpr != null)
        msgs = ((InternalEObject)newExpr).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GamlPackage.STATEMENT__EXPR, null, msgs);
      msgs = basicSetExpr(newExpr, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GamlPackage.STATEMENT__EXPR, newExpr, newExpr));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Facet> getFacets()
  {
    if (facets == null)
    {
      facets = new EObjectContainmentEList<Facet>(Facet.class, this, GamlPackage.STATEMENT__FACETS);
    }
    return facets;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Block getBlock()
  {
    return block;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBlock(Block newBlock, NotificationChain msgs)
  {
    Block oldBlock = block;
    block = newBlock;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GamlPackage.STATEMENT__BLOCK, oldBlock, newBlock);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBlock(Block newBlock)
  {
    if (newBlock != block)
    {
      NotificationChain msgs = null;
      if (block != null)
        msgs = ((InternalEObject)block).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GamlPackage.STATEMENT__BLOCK, null, msgs);
      if (newBlock != null)
        msgs = ((InternalEObject)newBlock).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GamlPackage.STATEMENT__BLOCK, null, msgs);
      msgs = basicSetBlock(newBlock, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GamlPackage.STATEMENT__BLOCK, newBlock, newBlock));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case GamlPackage.STATEMENT__EXPR:
        return basicSetExpr(null, msgs);
      case GamlPackage.STATEMENT__FACETS:
        return ((InternalEList<?>)getFacets()).basicRemove(otherEnd, msgs);
      case GamlPackage.STATEMENT__BLOCK:
        return basicSetBlock(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case GamlPackage.STATEMENT__KEY:
        return getKey();
      case GamlPackage.STATEMENT__FIRST_FACET:
        return getFirstFacet();
      case GamlPackage.STATEMENT__EXPR:
        return getExpr();
      case GamlPackage.STATEMENT__FACETS:
        return getFacets();
      case GamlPackage.STATEMENT__BLOCK:
        return getBlock();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case GamlPackage.STATEMENT__KEY:
        setKey((String)newValue);
        return;
      case GamlPackage.STATEMENT__FIRST_FACET:
        setFirstFacet((String)newValue);
        return;
      case GamlPackage.STATEMENT__EXPR:
        setExpr((Expression)newValue);
        return;
      case GamlPackage.STATEMENT__FACETS:
        getFacets().clear();
        getFacets().addAll((Collection<? extends Facet>)newValue);
        return;
      case GamlPackage.STATEMENT__BLOCK:
        setBlock((Block)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case GamlPackage.STATEMENT__KEY:
        setKey(KEY_EDEFAULT);
        return;
      case GamlPackage.STATEMENT__FIRST_FACET:
        setFirstFacet(FIRST_FACET_EDEFAULT);
        return;
      case GamlPackage.STATEMENT__EXPR:
        setExpr((Expression)null);
        return;
      case GamlPackage.STATEMENT__FACETS:
        getFacets().clear();
        return;
      case GamlPackage.STATEMENT__BLOCK:
        setBlock((Block)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case GamlPackage.STATEMENT__KEY:
        return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
      case GamlPackage.STATEMENT__FIRST_FACET:
        return FIRST_FACET_EDEFAULT == null ? firstFacet != null : !FIRST_FACET_EDEFAULT.equals(firstFacet);
      case GamlPackage.STATEMENT__EXPR:
        return expr != null;
      case GamlPackage.STATEMENT__FACETS:
        return facets != null && !facets.isEmpty();
      case GamlPackage.STATEMENT__BLOCK:
        return block != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (key: ");
    result.append(key);
    result.append(", firstFacet: ");
    result.append(firstFacet);
    result.append(')');
    return result.toString();
  }

} //StatementImpl
