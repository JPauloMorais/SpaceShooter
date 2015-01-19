package br.jp.redsparrow.engine.core.components;

import android.content.Context;
import android.opengl.GLES20;
import br.jp.redsparrow.engine.core.Constants;
import br.jp.redsparrow.engine.core.GameObject;
import br.jp.redsparrow.engine.core.Renderable;
import br.jp.redsparrow.engine.core.VertexArray;
import br.jp.redsparrow.engine.core.util.TextureUtil;
import br.jp.redsparrow.engine.shaders.TextureShaderProg;

public class SpriteComponent extends Component implements Renderable {

	private static final int POSITION_COUNT = 3;
	private static final int TEXTURE_COORDS_COUNT = 2;
	private static final int STRIDE = (POSITION_COUNT
			+ TEXTURE_COORDS_COUNT) * Constants.BYTES_PER_FLOAT;
	private VertexArray mVertsArray;

	private float[] mOffset;

	private TextureShaderProg mTextureProgram;
	private int mTexture;

	private float[] mVertsData;

	public SpriteComponent(Context context, int imgId, GameObject parent, float offsetX, float offsetY) {
		super("Sprite", parent);

		mTextureProgram = new TextureShaderProg(context);
		mTexture = TextureUtil.loadTexture(context, imgId);

		mVertsData = new float[30];

		mOffset = new float[2];
		mOffset[0] = offsetX;
		mOffset[1] = offsetY;

		setUVs();
		updateVertsData();

	}

	private void updateVertsData(){

		//TODO: setting do z
		//TODO: Suporte para eixos de rotacao arbitrarios

		//X Y Z                                                                                           
		mVertsData[0] = mParent.getX() + mParent.getWidth() / 2 + mOffset[0];  //right 
		mVertsData[1] = mParent.getY() + mParent.getHeight() / 2 + mOffset[1]; //top
		mVertsData[2] = 1f;

		//X Y Z
		mVertsData[5] = mParent.getX() - mParent.getWidth() / 2 - mOffset[0];  //left
		mVertsData[6] = mVertsData[1];                                         //top		
		mVertsData[7] = mVertsData[2];

		//X Y Z
		mVertsData[10] = mVertsData[5];                                         //left
		mVertsData[11] = mParent.getY() - mParent.getHeight() / 2 - mOffset[1]; //bottom
		mVertsData[12] = mVertsData[2];

		//X Y Z
		mVertsData[15] = mVertsData[0];                                          //right
		mVertsData[16] = mVertsData[11];                                         //bottom
		mVertsData[17] = mVertsData[2];

		//X Y Z
		mVertsData[20] = mVertsData[0];                                         //right
		mVertsData[21] = mVertsData[1];                                         //top
		mVertsData[22] = mVertsData[2];

		//X Y Z
		mVertsData[25] = mVertsData[5];                                         //left
		mVertsData[26] = mVertsData[11];                                        //bottom
		mVertsData[27] = mVertsData[2];

		mVertsArray = new VertexArray(mVertsData);

	}

	private void setUVs(){
		//U V
		mVertsData[3] = 0;//right
		mVertsData[4] = 0;//top
		//U V
		mVertsData[8] = 1;//left
		mVertsData[9] = 0;//top
		//U V
		mVertsData[13] = 1;//left
		mVertsData[14] = 1;//bottom
		//U V
		mVertsData[18] = 0;//right
		mVertsData[19] = 1;//bottom
		//U V
		mVertsData[23] = mVertsData[3];//right
		mVertsData[24] = mVertsData[4];//top
		//U V
		mVertsData[28] = 1;//left
		mVertsData[29] = 1;//bottom
	}

	private void bindData() {

		mVertsArray.setVertexAttribPointer(
				0,
				mTextureProgram.getPositionAttributeLocation(),
				POSITION_COUNT,
				STRIDE);
		mVertsArray.setVertexAttribPointer(
				POSITION_COUNT,
				mTextureProgram.getTextureCoordinatesAttributeLocation(),
				TEXTURE_COORDS_COUNT,
				STRIDE);

	}

	@Override
	public void render(VertexArray vertexArray, float[] projectionMatrix) {

		updateVertsData();
		mTextureProgram.useProgram();
		mTextureProgram.setUniforms(projectionMatrix, mTexture);
		bindData();
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

	}

}
