#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
in vec3 aPosition;  //����λ��
in vec4 aColor;    //������ɫ
out  vec4 vaColor;  //���ڴ��ݸ�ƬԪ��ɫ���ı���
void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��
   vaColor = aColor;//�����յ���ɫ���ݸ�ƬԪ��ɫ��
}                      