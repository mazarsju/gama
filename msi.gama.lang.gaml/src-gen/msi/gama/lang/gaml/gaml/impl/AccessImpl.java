/**
 */
package msi.gama.lang.gaml.gaml.impl;

import msi.gama.lang.gaml.gaml.Access;
import msi.gama.lang.gaml.gaml.ExpressionList;
import msi.gama.lang.gaml.gaml.GamlPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Access</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link msi.gama.lang.gaml.gaml.impl.AccessImpl#getArgs <em>Args</em>}</li>
 *   <li>{@link msi.gama.lang.gaml.gaml.impl.AccessImpl#getNamed_exp <em>Named exp</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AccessImpl extends ExpressionImpl implements Access
{
  /**
   * The cached value of the '{@link #getArgs() <em>Args</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArgs()
   * @generated
   * @ordered
   */
  protected ExpressionList args;

  /**
   * The default value of the '{@link #getNamed_exp() <em>Named exp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamed_exp()
   * @generated
   * @ordered
   */
  protected static final String NAMED_EXP_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getNamed_exp() <em>Named exp</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamed_exp()
   * @generated
   * @ordered
   */
  protected String named_exp = NAMED_EXP_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AccessImpl()
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
    return GamlPackage.Literals.ACCESS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExpressionList getArgs()
  {
    return args;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetArgs(ExpressionList newArgs, NotificationChain msgs)
  {
    ExpressionList oldArgs = args;
    args = newArgs;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GamlPackage.ACCESS__ARGS, oldArgs, newArgs);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setArgs(ExpressionList newArgs)
  {
    if (newArgs != args)
    {
      NotificationChain msgs = null;
      if (args != null)
        msgs = ((InternalEObject)args).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GamlPackage.ACCESS__ARGS, null, msgs);
      if (newArgs != null)
        msgs = ((InternalEObject)newArgs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GamlPackage.ACCESS__ARGS, null, msgs);
      msgs = basicSetArgs(newArgs, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GamlPackage.ACCESS__ARGS, newArgs, newArgs));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getNamed_exp()
  {
    return named_exp;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNamed_exp(String newNamed_exp)
  {
    String oldNamed_exp = named_exp;
    named_exp = newNamed_exp;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, GamlPackage.ACCESS__NAMED_EXP, oldNamed_exp, named_exp));
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
      case GamlPackage.ACCESS__ARGS:
        return basicSetArgs(null, msgs);
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
      case GamlPackage.ACCESS__ARGS:
        return getArgs();
      case GamlPackage.ACCESS__NAMED_EXP:
        return getNamed_exp();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case GamlPackage.ACCESS__ARGS:
        setArgs((ExpressionList)newValue);
        return;
      case GamlPackage.ACCESS__NAMED_EXP:
        setNamed_exp((String)newValue);
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
      case GamlPackage.ACCESS__ARGS:
        setArgs((ExpressionList)null);
        return;
      case GamlPackage.ACCESS__NAMED_EXP:
        setNamed_exp(NAMED_EXP_EDEFAULT);
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
      case GamlPackage.ACCESS__ARGS:
        return args != null;
      case GamlPackage.ACCESS__NAMED_EXP:
        return NAMED_EXP_EDEFAULT == null ? named_exp != null : !NAMED_EXP_EDEFAULT.equals(named_exp);
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
    result.append(" (named_exp: ");
    result.append(named_exp);
    result.append(')');
    return result.toString();
  }

} //AccessImpl
