package com.media.ui;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public final class GetOperator
{
    private static final int FIRST_SLOT_ID = 0;
    private static final int GEMINI_SIM_1 = 0;
    private static final int GEMINI_SIM_NUM = 2;
    private static final String GEMINI_SIM_NUM_PROP = "persist.gemini.sim_num";
    private static final String MTK_TELEPHONY_MANAGER_EX_CLASS_NAME = "com.mediatek.telephony.TelephonyManagerEx";
    private static final String QUALCOMM_CLASS_NAME = "android.telephony.MSimTelephonyManager";
    private static final String SPRD_OPERATORUTILS_CLASS_NAME = "android.telephony.SprdPhoneSupport";

    private static String getGenericMccCode(Context paramContext) throws Exception {
        int i = 0;
     String param = ((TelephonyManager)paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperator();
        if (param != null) {}
        i = 0;
        for (;;)
        {
            int j = Integer.parseInt(param.substring(0, 3));
            i = j;
            return String.valueOf(i);
        }
    }

    private static String getGenericMncCode(Context paramContext) throws Exception {
        String param = ((TelephonyManager)paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperator();
        int j = 0;
        int i = j;
        if (paramContext != null) {}
        i = Integer.parseInt(param.substring(3));
        return String.valueOf(i);
    }

    private static String getGenericSingleImei(Context paramContext)
    {
        String param = ((TelephonyManager)paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (param != null) {
            return param;
        }
        return "";
    }

    protected static String getImeis(Context paramContext) throws Exception {
        int j = 0;
        int i = 0;
        Object localObject1;
        if (isMediatek())
        {
            ArrayList localArrayList = getSimSlots();
            Object localObject2 = "";
            localObject1 = localObject2;
            if (localArrayList != null)
            {
                localObject1 = localObject2;
                if (localArrayList.size() > 0)
                {
                    localObject2 = localArrayList.iterator();
                    for (localObject1 = ""; ((Iterator)localObject2).hasNext(); localObject1 = (String)localObject1 + " " + getMTKImeiForSimSlot(paramContext, i)) {
                        i = ((Integer)((Iterator)localObject2).next()).intValue();
                    }
                }
            }
            return ((String)localObject1).trim();
        }
        if (isQualcomm())
        {
            localObject1 = "";
            j = getQualcommPhoneCount(paramContext);
            while (i < j)
            {
                localObject1 = (String)localObject1 + " " + getQualcommImeiForSimSlot(paramContext, i);
                i += 1;
            }
            return ((String)localObject1).trim();
        }
        if (isSpreadtrum())
        {
            int k = getSPRDPhoneCount(paramContext);
            if (k == 0) {
                return getGenericSingleImei(paramContext);
            }
            if (k == 1) {
                return getGenericSingleImei(paramContext);
            }
            localObject1 = "";
            i = j;
            while (i < k)
            {
                localObject1 = (String)localObject1 + " " + getSPRDImeiForSimSlot(paramContext, i);
                i += 1;
            }
            return ((String)localObject1).trim();
        }
        localObject1 = Build.MANUFACTURER.toUpperCase(Locale.US);
        Object localObject2 = Build.MODEL.toUpperCase(Locale.US);
      //  SLog.e("Build", "manu: " + (String)localObject1 + ", model: " + (String)localObject2);
        if ((((String)localObject2).contains("SWIRL")) || (((String)localObject2).contains("PANASONIC ELUGA L2"))) {
            return getQualcommImei(paramContext);
        }
        return getGenericSingleImei(paramContext);
    }

    private static String getMCCForSlot(Context paramContext, int paramInt) throws Exception {
        if (isQualcomm()) {
            return getQualCommMCCForSlot(paramContext, paramInt);
        }
        if (isMediatek()) {
            return getMTKMCCForSimSlot(paramContext, paramInt);
        }
        if (isSpreadtrum()) {
            return getSpreadtrumMCCForSimSlot(paramContext, paramInt);
        }
        return getGenericMccCode(paramContext);
    }

    private static String getMNCForSlot(Context paramContext, int paramInt) throws Exception {
        if (isQualcomm()) {
            return getQualCommMNCForSlot(paramContext, paramInt);
        }
        if (isMediatek()) {
            return getMTKMNCForSimSlot(paramContext, paramInt);
        }
        if (isSpreadtrum()) {
            return getSpreadtrumMNCForSimSlot(paramContext, paramInt);
        }
        return getGenericMncCode(paramContext);
    }

    private static String getMTKImeiForSimSlot(Context paramContext, int paramInt)
    {
        for (;;)
        {
            try
            {
                Method[] arrayOfMethod = Class.forName("com.mediatek.telephony.TelephonyManagerEx").getMethods();
                int j = arrayOfMethod.length;
                int i = 0;
                if (i < j)
                {
                    Object localObject1 = arrayOfMethod[i];
                    if (((Method)localObject1).getName().equals("getDefault"))
                    {
                        localObject1 = ((Method)localObject1).invoke(null, (Object[])null);
                        j = arrayOfMethod.length;
                        i = 0;
                        if (i >= j) {
                            break;
                        }
                        Method localMethod = arrayOfMethod[i];
                        if (localMethod.getName().equals("getDeviceId"))
                        {
                            localObject1 = localMethod.invoke(localObject1, new Object[] { Integer.valueOf(paramInt) }).toString();
                            return (String)localObject1;
                        }
                    }
                    else
                    {
                        i += 1;
                        continue;
                    }
                    i += 1;
                    continue;

                }
            }
            catch (Exception localException)
            {
                return getGenericSingleImei(paramContext);
            }

        }
        return null;
    }

    private static String getMTKMCCForSimSlot(Context paramContext, int paramInt) throws Exception {
        Object localObject2 = null;
        for (;;)
        {
            int i;
            try
            {
                Method[] arrayOfMethod = Class.forName("com.mediatek.telephony.TelephonyManagerEx").getMethods();
                int j = arrayOfMethod.length;
                i = 0;
                if (i >= j) {
                    localObject2 = null;
                }
                Object localObject1 = arrayOfMethod[i];
                if (!((Method)localObject1).getName().equals("getDefault")) {
                    i += 1;
                    break;
                }
                localObject1 = ((Method)localObject1).invoke(null, (Object[])null);
                j = arrayOfMethod.length;
                i = 0;
                if (i < j)
                {
                    Method localMethod = arrayOfMethod[i];
                    if (!localMethod.getName().equals("getNetworkOperator")) {
                        i += 1;
                        break;
                    }
                    localObject1 = localMethod.invoke(localObject1, new Object[] { Integer.valueOf(paramInt) }).toString();
                    if ((localObject1 != null) && (!"".equalsIgnoreCase((String)localObject1)) && (((String)localObject1).length() > 3)) {
                        return ((String)localObject1).substring(0, 3);
                    }
                    return "";
                }
            }
            catch (Exception localException)
            {
                return getGenericMccCode(paramContext);
            }

        }
        return null;
    }

    private static String getMTKMNCForSimSlot(Context paramContext, int paramInt) throws Exception {
        for (;;)
        {
            int i;
            try
            {
                Method[] arrayOfMethod = Class.forName("com.mediatek.telephony.TelephonyManagerEx").getMethods();
                int j = arrayOfMethod.length;
                i = 0;
                if (i >= j) {
                    Object localObject2 = null;
                    break;
                }
                Object localObject1 = arrayOfMethod[i];
                if (!((Method)localObject1).getName().equals("getDefault")) {
                    i += 1;
                    break;
                }
                localObject1 = ((Method)localObject1).invoke(null, (Object[])null);
                j = arrayOfMethod.length;
                i = 0;
                if (i < j)
                {
                    Method localMethod = arrayOfMethod[i];
                    if (!localMethod.getName().equals("getNetworkOperator")) {
                        i += 1;
                        break;
                    }
                    localObject1 = localMethod.invoke(localObject1, new Object[] { Integer.valueOf(paramInt) }).toString();
                    if ((localObject1 != null) && (!"".equalsIgnoreCase((String)localObject1)) && (((String)localObject1).length() > 3)) {
                        return ((String)localObject1).substring(3);
                    }
                    return "";
                }
            }
            catch (Exception localException)
            {
                return getGenericMncCode(paramContext);
            }

        }
        return null;
    }

    private static int getMTKSimSlots()
    {
        return 2;
    }

    protected static String getMcc(Context paramContext) throws Exception {
        int j = getSimCount(paramContext);
        String str = "";
        int i = 0;
        while (i < j)
        {
            str = str + " " + getMCCForSlot(paramContext, i);
            i += 1;
        }
        return str.trim();
    }

    protected static String getMnc(Context paramContext) throws Exception {
        int j = getSimCount(paramContext);
        String str = "";
        int i = 0;
        while (i < j)
        {
            str = str + " " + getMNCForSlot(paramContext, i);
            i += 1;
        }
        return str.trim();
    }

    private static String getQualCommMCCForSlot(Context paramContext, int paramInt) throws Exception {
        for (;;)
        {
            int i;
            try
            {
                Method[] arrayOfMethod = Class.forName("android.telephony.MSimTelephonyManager").getMethods();
                int j = arrayOfMethod.length;
                i = 0;
                if (i >= j) {
                    Object localObject2 = null;
                    break;
                }
                Object localObject1 = arrayOfMethod[i];
                if (!((Method)localObject1).getName().equals("getDefault")) {
                    i += 1;
                    break ;
                }
                localObject1 = ((Method)localObject1).invoke(null, (Object[])null);
                j = arrayOfMethod.length;
                i = 0;
                if (i < j)
                {
                    Method localMethod = arrayOfMethod[i];
                    if (!localMethod.getName().equals("getNetworkOperator")) {
                        i += 1;
                        break;
                    }
                    localObject1 = localMethod.invoke(localObject1, new Object[] { Integer.valueOf(paramInt) }).toString();
                    if ((localObject1 != null) && (!"".equalsIgnoreCase((String)localObject1)) && (((String)localObject1).length() > 3)) {
                        return ((String)localObject1).substring(0, 3);
                    }
                    return "";
                }
            }
            catch (Exception localException)
            {
                return getGenericMccCode(paramContext);
            }

        }
        return null;
    }

    private static String getQualCommMNCForSlot(Context paramContext, int paramInt) throws Exception {
        for (;;)
        {
            int i;
            try
            {
                Method[] arrayOfMethod = Class.forName("android.telephony.MSimTelephonyManager").getMethods();
                int j = arrayOfMethod.length;
                i = 0;
                if (i >= j) {
                    Object localObject2 = null;
                    break;
                }
                Object localObject1 = arrayOfMethod[i];
                if (!((Method)localObject1).getName().equals("getDefault")) {
                    i += 1;
                    break;
                }
                localObject1 = ((Method)localObject1).invoke(null, (Object[])null);
                j = arrayOfMethod.length;
                i = 0;
                if (i < j)
                {
                    Method localMethod = arrayOfMethod[i];
                    if (!localMethod.getName().equals("getNetworkOperator")) {
                        i += 1;
                        break;
                    }
                    localObject1 = localMethod.invoke(localObject1, new Object[] { Integer.valueOf(paramInt) }).toString();
                    if ((localObject1 != null) && (!"".equalsIgnoreCase((String)localObject1)) && (((String)localObject1).length() > 3)) {
                        return ((String)localObject1).substring(3);
                    }
                    return "";
                }
            }
            catch (Exception localException)
            {
                return getGenericMncCode(paramContext);
            }

        }
        return null;
    }

    private static String getQualcommImei(Context paramContext)
    {
        Object localObject = (TelephonyManager)paramContext.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            Method localMethod = Class.forName("android.telephony.TelephonyManager").getMethod("getImei", new Class[] { Integer.TYPE });
            String str = (String)localMethod.invoke(localObject, new Object[] { Integer.valueOf(0) });
            localObject = str + " " + (String)localMethod.invoke(localObject, new Object[] { Integer.valueOf(1) });
            return (String)localObject;
        }
        catch (Exception localException) {}
        return getGenericSingleImei(paramContext);
    }

    private static String getQualcommImeiForSimSlot(Context paramContext, int paramInt)
    {
        for (;;)
        {
            try
            {
                Method[] arrayOfMethod = Class.forName("android.telephony.MSimTelephonyManager").getMethods();
                int j = arrayOfMethod.length;
                int i = 0;
                if (i < j)
                {
                    Object localObject1 = arrayOfMethod[i];
                    if (((Method)localObject1).getName().equals("getDefault"))
                    {
                        localObject1 = ((Method)localObject1).invoke(null, (Object[])null);
                        j = arrayOfMethod.length;
                        i = 0;
                        if (i >= j) {
                            Object localObject2 = null;
                            break;
                        }
                        Method localMethod = arrayOfMethod[i];
                        if (localMethod.getName().equals("getDeviceId"))
                        {
                            localObject1 = localMethod.invoke(localObject1, new Object[] { Integer.valueOf(paramInt) }).toString();
                            return (String)localObject1;
                        }
                    }
                    else
                    {
                        i += 1;
                        continue;
                    }
                    i += 1;
                    continue;

                }
            }
            catch (Exception localException)
            {
                return getGenericSingleImei(paramContext);
            }

        }
        return null;
    }

    private static int getQualcommPhoneCount(Context paramContext) throws Exception {
        for (;;)
        {
            Method[] arrayOfMethod = Class.forName("android.telephony.MSimTelephonyManager").getMethods();
            int j = arrayOfMethod.length;
            int i = 0;
            if (i < j)
            {
               Method param = arrayOfMethod[i];
                if (param.getName().equals("getDefault"))
                {
                  Object paramObj = param.invoke(null, (Object[])null);
                    j = arrayOfMethod.length;
                    i = 0;
                    if (i >= j) {
                        paramContext = null;
                        break;
                                            }
                    Method localMethod = arrayOfMethod[i];
                    if (localMethod.getName().equals("getPhoneCount"))
                    {
                        i = ((Integer)localMethod.invoke(paramContext, (Object[])null)).intValue();
                        return i;
                    }
                }
                else
                {
                    i += 1;
                    continue;
                }
                i += 1;
                continue;

            }


        }
        return 0;
    }

    private static String getSPRDImeiForSimSlot(Context paramContext, int paramInt)
    {
        int i = 0;
        try
        {
            Object localObject = Class.forName(((TelephonyManager)paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getClass().getName());
            String str = "";
            Method[] arrayOfMethod = ((Class)localObject).getMethods();
            int j = arrayOfMethod.length;
            for (;;)
            {
                localObject = str;
                if (i < j)
                {
                    localObject = arrayOfMethod[i];
                    if (((Method)localObject).getName().equals("getServiceName")) {
                        localObject = (String)((Method)localObject).invoke(null, new Object[] { Context.TELEPHONY_SERVICE, Integer.valueOf(paramInt) });
                    }
                }
                else
                {
                    localObject = ((TelephonyManager)paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    return (String)localObject;
                }
                i += 1;
                return getGenericSingleImei(paramContext);
            }
        }
        catch (Exception localException) {}
        return null;
    }

    private static int getSPRDPhoneCount(Context paramContext) throws Exception {
        Method[] param = Class.forName(((TelephonyManager)paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getClass().getName()).getMethods();
        int j = param.length;
        int i = 0;
        while (i < j)
        {
            Object localObject = param[i];
            if (((Method)localObject).getName().equals("getPhoneCount"))
            {
                i = ((Integer)((Method)localObject).invoke(null, (Object[])null)).intValue();
                return i;
            }
            i += 1;
        }
        return 0;
    }

    private static int getSimCount(Context paramContext) throws Exception {
        if (isMediatek()) {
            return getMTKSimSlots();
        }
        if (isQualcomm()) {
            return getQualcommPhoneCount(paramContext);
        }
        if (isSpreadtrum()) {
            return getSPRDPhoneCount(paramContext);
        }
        return 1;
    }

    private static ArrayList<Integer> getSimSlots()
    {
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        while (i < 2)
        {
            localArrayList.add(Integer.valueOf(i + 0));
            i += 1;
        }
        return localArrayList;
    }

    private static String getSpreadtrumMCCForSimSlot(Context paramContext, int paramInt) throws Exception {
        int i = 0;
        for (;;)
        {
            try
            {
                Object localObject = Class.forName(((TelephonyManager)paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getClass().getName());
                String str = "";
                Method[] arrayOfMethod = ((Class)localObject).getMethods();
                int j = arrayOfMethod.length;
                localObject = str;
                if (i < j)
                {
                    localObject = arrayOfMethod[i];
                    if (((Method)localObject).getName().equals("getServiceName")) {
                        localObject = (String)((Method)localObject).invoke(null, new Object[] { Context.TELEPHONY_SERVICE, Integer.valueOf(paramInt) });
                    }
                }
                else
                {
                    localObject = (((TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperator());
                    if ((localObject != null) && (!"".equalsIgnoreCase((String)localObject)) && (((String)localObject).length() > 3)) {
                        return ((String)localObject).substring(0, 3);
                    }
                    return "";
                }
            }
            catch (Exception localException)
            {
                return getGenericMccCode(paramContext);
            }
            i += 1;
        }
    }

    private static String getSpreadtrumMNCForSimSlot(Context paramContext, int paramInt) throws Exception {
        int i = 0;
        for (;;)
        {
            try
            {
                Object localObject = Class.forName(((TelephonyManager)paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getClass().getName());
                String str = "";
                Method[] arrayOfMethod = ((Class)localObject).getMethods();
                int j = arrayOfMethod.length;
                localObject = str;
                if (i < j)
                {
                    localObject = arrayOfMethod[i];
                    if (((Method)localObject).getName().equals("getServiceName")) {
                        localObject = (String)((Method)localObject).invoke(null, new Object[] { Context.TELEPHONY_SERVICE, Integer.valueOf(paramInt) });
                    }
                }
                else
                {
                    localObject = (((TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperator());
                    if ((localObject != null) && (!"".equalsIgnoreCase((String)localObject)) && (((String)localObject).length() > 3)) {
                        return ((String)localObject).substring(3);
                    }
                    return "";
                }
            }
            catch (Exception localException)
            {
                return getGenericMncCode(paramContext);
            }
            i += 1;
        }
    }

    private static boolean isMediatek()
    {
        try
        {
            Class.forName("com.mediatek.telephony.TelephonyManagerEx");
            return true;
        }
        catch (ClassNotFoundException localClassNotFoundException) {}
        return false;
    }

    private static boolean isQualcomm()
    {
        try
        {
            Class.forName("android.telephony.MSimTelephonyManager");
            return true;
        }
        catch (ClassNotFoundException localClassNotFoundException) {}
        return false;
    }

    private static boolean isSpreadtrum()
    {
        try
        {
            Class.forName("android.telephony.SprdPhoneSupport");
            return true;
        }
        catch (ClassNotFoundException localClassNotFoundException) {}
        return false;
    }
}
