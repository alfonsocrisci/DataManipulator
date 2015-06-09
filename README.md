# DataManipulator
You can create data into Liferay portal.

---

# Support:

I'm not going to support Liferay 6.0<br />
Currently you can deploy this portlet under Liferay 6.1 and 6.2.<br />
I'm working on master/trunk and 7.0 support.

---

# ArrayIndexOutOfBoundsException:

If you would like to use this portlet on 6.1.x or above, please apply the following diff to your portal source.<br />
With this, you can avoid 'java.lang.ArrayIndexOutOfBoundsException: -1' which trown by this util.

```diff
diff --git a/portal-impl/src/com/liferay/portal/spring/transaction/TransactionCommitCallbackUtil.java b/portal-impl/src/com/liferay/portal/spring/transaction/TransactionCommitCallbackUtil.java
--- a/portal-impl/src/com/liferay/portal/spring/transaction/TransactionCommitCallbackUtil.java
+++ b/portal-impl/src/com/liferay/portal/spring/transaction/TransactionCommitCallbackUtil.java
@@ -61,6 +61,10 @@ class TransactionCommitCallbackUtil {
 		List<List<Callable<?>>> callbackListList =
 			_callbackListListThreadLocal.get();
 
+		if (callbackListList.isEmpty()) {
+			return new ArrayList<Callable<?>>(0);
+		}
+
 		return callbackListList.remove(callbackListList.size() - 1);
 	}
 
```