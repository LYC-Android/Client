## Client<br>
#1.1<br>
1.�Ѿ���ԭ����JNI����ȫ��ɾ�����Ժ����So�ļ�<br>
��Ϊ���¼�����<br>
�� sourceSets {<br>
        main {<br>
            jniLibs.srcDirs = ['libs']<br>
            jni.srcDirs = ['src/main/jni']<br>
        }<br>
    }<br>
����libĿ¼���½������ļ���arm64-v8a  armeabi x86���Ұ����Ӧ��so�ļ����ƹ�ȥ��<br>

���½�һ����so�ļ���Ӧ��java�ļ�������ؿⲢ�ң������Ǹ�native������ע�ⷽ����������ʲô��һ��Ҫ��ͬ�����򱨴�<br>

#1.2
1.�Ѿ��޸��˵������ڿ�����bug
