# ipctest
创建远程依赖项目第一次上传

How to
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle
maven

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.luguixue:ipctest:Tag'
	}

