## Client<br>
#1.1<br>
1.已经把原来的JNI代码全部删除，以后改用So文件<br>
分为如下几步：<br>
① sourceSets {<br>
        main {<br>
            jniLibs.srcDirs = ['libs']<br>
            jni.srcDirs = ['src/main/jni']<br>
        }<br>
    }<br>
②在lib目录下新建三个文件夹arm64-v8a  armeabi x86并且把相对应的so文件复制过去。<br>

③新建一个与so文件对应的java文件里面加载库并且，声明那个native方法，注意方法名，包名什么的一定要相同，否则报错。<br>
