#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
uniform mat4 uMMatrix; //�����任����
uniform vec3 uLightLocation;	//��Դλ��
uniform vec3 uCamera;	//�����λ��
in vec3 aPosition;  //����λ��
in vec3 aNormal;    //���㷨����
uniform vec4 u_clipPlane;//����ƽ��
//���ڴ��ݸ�ƬԪ��ɫ���ı���
out vec4 ambient;
out vec4 diffuse;
out vec4 specular;
out float u_clipDist;

//��λ����ռ���ķ���
void pointLight(					//��λ����ռ���ķ���
  in vec3 normal,				//������
  inout vec4 ambient,			//����������ǿ��
  inout vec4 diffuse,				//ɢ�������ǿ��
  inout vec4 specular,			//���������ǿ��
  in vec3 lightLocation,			//��Դλ��
  in vec4 lightAmbient,			//������ǿ��
  in vec4 lightDiffuse,			//ɢ���ǿ��
  in vec4 lightSpecular			//�����ǿ��
){
  ambient=lightAmbient;			//ֱ�ӵó������������ǿ��  
  vec3 normalTarget=aPosition+normal;	//����任��ķ�����
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 	//�Է��������
  //����ӱ���㵽�����������
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  //����ӱ���㵽��Դλ�õ�����vp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);  
  vp=normalize(vp);//��ʽ��vp
  vec3 halfVector=normalize(vp+eye);	//����������ߵİ�����    
  float shininess=50.0;				//�ֲڶȣ�ԽСԽ�⻬
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//��������vp�ĵ����0�����ֵ
  diffuse=lightDiffuse*nDotViewPosition;				//����ɢ��������ǿ��
  float nDotViewHalfVector=dot(newNormal,halfVector);	//������������ĵ�� 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//���淴���ǿ������
  specular=lightSpecular*powerFactor;    			//���㾵��������ǿ��
}


void main()     
{ 
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��  
   pointLight(normalize(aNormal),ambient,diffuse,specular,
   uLightLocation,vec4(0.1,0.1,0.1,1.0),vec4(0.7,0.7,0.7,1.0),vec4(0.3,0.3,0.3,1.0));

   //������λ��(x0,y0,z0)����ƽ�淽��Ax+By+Cz+D=0,�������������ݸ�ƬԪ��ɫ��
   //��Ax0+By0+Cz0+D>0���򶥵���ƽ����ϲ࣬��֮��ƽ����²�
   u_clipDist = dot(aPosition.xyz, u_clipPlane.xyz) +u_clipPlane.w;
}                      