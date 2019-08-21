 #include "ShaderUtil.h"//����ͷ�ļ�
#include <android/log.h>
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "native-activity", __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, "native-activity", __VA_ARGS__))

GLuint ShaderUtil::createProgram(const char* vertexShaderSource,
									  const char* fragmentShaderSource)
{
	GLuint vertexShader = loadShader(vertexShaderSource, GL_VERTEX_SHADER);	//���ض�����ɫ��
	GLuint fragmentShader = loadShader(fragmentShaderSource, GL_FRAGMENT_SHADER);	//����ƬԪ��ɫ��
	GLuint programHandle = glCreateProgram();//������ɫ������
	glAttachShader(programHandle, vertexShader);//����ɫ�������м��붥����ɫ��
	glAttachShader(programHandle, fragmentShader);//����ɫ�������м���ƬԪ��ɫ��
	glLinkProgram(programHandle);//������ɫ������
	GLint linkSuccess;//���������Ƿ�ɹ���־����
	glGetProgramiv(programHandle, GL_LINK_STATUS, &linkSuccess);
	if (linkSuccess == GL_FALSE) {//������ʧ�ܻ�ȡ��ȡ������Ϣ
		GLchar messages[256];
		glGetProgramInfoLog(programHandle, sizeof(messages), 0, &messages[0]);
		messages[256]='\0';
		LOGE("Shader Link Error:");
        LOGE("%s",(char*)messages);
	}
	return programHandle;//���ؽ��
}

GLuint ShaderUtil::loadShader(const char* source, GLenum shaderType)
{
	GLuint shaderHandle = glCreateShader(shaderType);
	glShaderSource(shaderHandle, 1, &source, 0);//������ɫ���Ľű�
	glCompileShader(shaderHandle);//������ɫ��
	GLint compileSuccess;//���������Ƿ�ɹ���־����
	glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, &compileSuccess);
	if (compileSuccess == GL_FALSE) {//������ʧ�����ȡ������Ϣ
		GLchar messages[256];
		glGetShaderInfoLog(shaderHandle, sizeof(messages), 0, &messages[0]);
		messages[256]='\0';
		LOGE("Shader Compile Error:");
		LOGE("%s",(char*)messages);
	}
	return shaderHandle;//���ؽ��
}

