[1mdiff --git a/src/main/java/com/rogervinas/testwebapp/model/Session.java b/src/main/java/com/rogervinas/testwebapp/model/Session.java[m
[1mindex 242b0c4..84b01c2 100644[m
[1m--- a/src/main/java/com/rogervinas/testwebapp/model/Session.java[m
[1m+++ b/src/main/java/com/rogervinas/testwebapp/model/Session.java[m
[36m@@ -3,8 +3,9 @@[m [mpackage com.rogervinas.testwebapp.model;[m
 public abstract class Session extends ActiveRecord<Session>[m
 {	[m
 	private User user;[m
[31m-	private long expires = System.currentTimeMillis();[m
[31m-	[m
[32m+[m	[32mprivate long creationTime = System.currentTimeMillis();[m[41m[m
[32m+[m	[32mprivate int maxAge;[m[41m[m
[32m+[m[41m[m
 	public Session(String id)[m
 	{[m
 		super(id);[m
[36m@@ -20,32 +21,40 @@[m [mpublic abstract class Session extends ActiveRecord<Session>[m
 		this.user = user;[m
 	}[m
 	[m
[31m-	public void setExpires(long time) {[m
[31m-		this.expires = time;[m
[32m+[m	[32mpublic long getCreationTime() {[m[41m[m
[32m+[m		[32mreturn creationTime;[m[41m[m
 	}[m
 	[m
[31m-	public long getExpires() {[m
[31m-		return this.expires;[m
[32m+[m	[32mpublic void resetCreationTime() {[m[41m[m
[32m+[m		[32msetCreationTime(System.currentTimeMillis());[m[41m[m
[32m+[m	[32m}[m[41m[m
[32m+[m[41m	[m
[32m+[m	[32mpublic void setCreationTime(long time) {[m[41m[m
[32m+[m		[32mthis.creationTime = time;[m[41m[m
[32m+[m	[32m}[m[41m[m
[32m+[m[41m		[m
[32m+[m	[32mpublic int getMaxAge() {[m[41m[m
[32m+[m		[32mreturn maxAge;[m[41m[m
 	}[m
 	[m
 	/*[m
[31m-	 * Sets session expiration time to now + <code>age</code> seconds[m
[31m-	 * @param age time to add to expiration time in seconds[m
[32m+[m	[32m * @param maxAge max age in seconds[m[41m[m
 	 */[m
[31m-	public void setMaxAge(long age) {[m
[31m-		setExpires(System.currentTimeMillis() + age * 1000);[m
[32m+[m	[32mpublic void setMaxAge(int maxAge) {[m[41m[m
[32m+[m		[32mthis.maxAge = maxAge;[m[41m[m
 	}[m
[31m-	[m
[32m+[m[41m[m
 	public boolean hasExpired() {[m
[31m-		return this.expires < System.currentTimeMillis();[m
[32m+[m		[32mreturn creationTime + maxAge*1000 < System.currentTimeMillis();[m[41m[m
 	}[m
[31m-	[m
[32m+[m[41m[m
 	public String toString() [m
 	{[m
 		return getClass().getSimpleName() [m
 				+ " { id:" + getId() [m
 				+ " user:" + user[m
[31m-				+ " expirationTime:" + expires[m
[32m+[m				[32m+ " creationTime:" + creationTime[m[41m[m
[32m+[m				[32m+ " maxAge:" + maxAge[m[41m[m
 				+ " }";[m
 	}[m
 }[m
