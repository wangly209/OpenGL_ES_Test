package com.bn.Sample5_6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES30;

//��ɫ�����
public class PointsOrLines {
	int mProgram;// �Զ�����Ⱦ������ɫ������id
	int muMVPMatrixHandle;// �ܱ任��������
	int maPositionHandle; // ����λ����������
	int maColorHandle; // ������ɫ��������
	String mVertexShader;// ������ɫ������ű�
	String mFragmentShader;// ƬԪ��ɫ������ű�

	FloatBuffer mVertexBuffer;// �����������ݻ���
	FloatBuffer mColorBuffer;// ������ɫ���ݻ���
	int vCount = 0;

	public PointsOrLines(MySurfaceView mv) {
		// ��ʼ��������������ɫ����
		initVertexData();
		// ��ʼ��shader
		initShader(mv);
	}

	// ��ʼ��������������ɫ���ݵķ���
	public void initVertexData() {
		// �����������ݵĳ�ʼ��================begin============================
		vCount = 5;

		float vertices[] = new float[] {
				0, 0, 0, Constant.UNIT_SIZE, Constant.UNIT_SIZE, 0,
				-Constant.UNIT_SIZE, Constant.UNIT_SIZE, 0,
				-Constant.UNIT_SIZE, -Constant.UNIT_SIZE, 0,
				Constant.UNIT_SIZE, -Constant.UNIT_SIZE, 0, };

		// ���������������ݻ���
		// vertices.length*4����Ϊһ�������ĸ��ֽ�
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexBuffer = vbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mVertexBuffer.put(vertices);// �򻺳����з��붥����������
		mVertexBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// �����������ݵĳ�ʼ��================end============================

		// ������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
		float colors[] = new float[] {
				1, 1, 0, 0,// ��
				1, 1, 1, 0,// ��
				0, 1, 0, 0,// ��
				1, 1, 1, 0,// ��
				1, 1, 0, 0,// ��
		};
		// ����������ɫ���ݻ���
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mColorBuffer = cbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mColorBuffer.put(colors);// �򻺳����з��붥����ɫ����
		mColorBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// ������ɫ���ݵĳ�ʼ��================end============================
	}

	// ��ʼ����ɫ��
	public void initShader(MySurfaceView mv) {
		// ���ض�����ɫ���Ľű�����
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh",
				mv.getResources());
		// ����ƬԪ��ɫ���Ľű�����
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh",
				mv.getResources());
		// ���ڶ�����ɫ����ƬԪ��ɫ����������
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// ��ȡ�����ж���λ����������id
		maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
		// ��ȡ�����ж�����ɫ��������id
		maColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");
		// ��ȡ�������ܱ任��������id
		muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
	}

	public void drawSelf() {
		//ָ��ʹ��ĳ����ɫ������
		GLES30.glUseProgram(mProgram);
		//�����ձ任��������Ⱦ����
		GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);
		//������λ������������Ⱦ����
		GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,
				false, 3 * 4, mVertexBuffer);
		//��������ɫ����������Ⱦ����
		GLES30.glVertexAttribPointer(maColorHandle, 4, GLES30.GL_FLOAT, false,
				4 * 4, mColorBuffer);
		//���ö���λ����������
		GLES30.glEnableVertexAttribArray(maPositionHandle);
		//���ö�����ɫ��������
		GLES30.glEnableVertexAttribArray(maColorHandle);
		
		GLES30.glLineWidth(10);//�����ߵĿ���
		//���Ƶ����
		switch (Constant.CURR_DRAW_MODE) {
		case Constant.GL_POINTS:// GL_POINTS��ʽ
			GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCount);
			break;
		case Constant.GL_LINES:// GL_LINES��ʽ			
			GLES30.glDrawArrays(GLES30.GL_LINES, 0, vCount);
			break;
		case Constant.GL_LINE_STRIP:// GL_LINE_STRIP��ʽ
			GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, vCount);
			break;
		case Constant.GL_LINE_LOOP:// GL_LINE_LOOP��ʽ
			GLES30.glDrawArrays(GLES30.GL_LINE_LOOP, 0, vCount);
			break;
		}
	}
}