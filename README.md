# DataManipulator
You can create data into Liferay portal.

---

If you would like to use this portlet on 6.1.x or above, please apply the following diff to your portal source.
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