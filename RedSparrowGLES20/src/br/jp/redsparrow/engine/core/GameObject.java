package br.jp.redsparrow.engine.core;

import java.util.ArrayList;

import android.graphics.RectF;
import br.jp.redsparrow.engine.core.components.Component;
import br.jp.redsparrow.engine.core.messages.Message;
import br.jp.redsparrow.game.ObjectFactory.OBJ_TYPE;

public class GameObject {

	private OBJ_TYPE type;
	private int mId = -2;

	private Vector2f mPosition;
	//Z
	private float mLayer;
	private float mWidth;
	private float mHeight;
	private RectF mBounds;
	private float mRotation;
	private Vector2f mColOffset;

	private float[] mVertsData;

	private boolean isDead = false;

	//	private Vector2f mTexturePosition;

	private VertexArray mVertexArray;

	private ArrayList<Component> mUpdatableComponents;
	private ArrayList<Component> mRenderableComponents;

	private ArrayList<Message> mCurMessages;
	private ArrayList<Message> mMessagesToRemove;

	public GameObject(){
		this(0,0,0,0);
	}

	public GameObject(float x, float y){
		this(x,y,0,0);
	}

	public GameObject(float x, float y, float width, float height){

		mCurMessages = new ArrayList<Message>();
		mMessagesToRemove = new ArrayList<Message>();

		mUpdatableComponents = new ArrayList<Component>();
		mRenderableComponents = new ArrayList<Component>();

		mPosition = new Vector2f(x, y);
		mLayer = 1f;
		mWidth = width;
		mHeight = height;
		mBounds = new RectF(x, y, x+width, x+height);

		mColOffset = new Vector2f(-0.1f, -0.3f);

		//		mTexturePosition = new Vector2f(0,0);

		mVertsData = new float[30];
		updateVertsData(x, y);

	}

	public void update(){

		updateVertsData(mPosition.getX(), mPosition.getY());

		for (Component component : mUpdatableComponents) {
			((Updatable) component).update(this);
		}

		removeRecievedMessages();

	}

	public void render(float[] projectionMatrix){

//		Matrix.rotateM(projectionMatrix, 0, 20, 0, 0, 1);

		for (Component component : mRenderableComponents) {
			((Renderable) component).render(mVertexArray, projectionMatrix);
		}

	}

	//-------RENDERIZACAO----------
	//Redefine vertices
	public void updateVertsData(float x, float y){
		if (mBounds.centerX() != x || mBounds.centerY() != y) {
			//                     left                 top            right         bottom  
			mBounds.set( x-((mWidth/2)) , y-(mHeight/2), x+(mWidth/2), y+((mHeight/2)) );

			//   X   ,  Y   , Z,      S   , T
			mVertsData[0] = x;  mVertsData[1] =  y ; mVertsData[2] = mLayer;                                  mVertsData[3] = 0.5f; mVertsData[4] = 0.5f; //centro
			mVertsData[5] = mBounds.right;   mVertsData[6] = mBounds.top ; mVertsData[7] = mLayer;            mVertsData[8] = 0f  ; mVertsData[9] = 1f  ; //inf. esq.
			mVertsData[10] = mBounds.left;  mVertsData[11] = mBounds.top ; mVertsData[12] = mLayer;           mVertsData[13] = 1f ; mVertsData[14] = 1f ; //inf. dir.
			mVertsData[15] = mBounds.left; mVertsData[16] = mBounds.bottom   ; mVertsData[17] = mLayer;       mVertsData[18] = 1f ; mVertsData[19] =  0f; //sup. dir.
			mVertsData[20] = mBounds.right ; mVertsData[21] = mBounds.bottom   ; mVertsData[22] = mLayer;     mVertsData[23] = 0f ; mVertsData[24] = 0f ; //sup. esq.
			mVertsData[25] = mBounds.right ; mVertsData[26] = mBounds.top; mVertsData[27] = mLayer;           mVertsData[28] = 0f ; mVertsData[29] = 1f;    //inf. esq.

			mVertexArray = new VertexArray(mVertsData);
		}

	}

	//Redefine vertices e mapeamento da textura
	public void updateVertsData(float x, float y, float s, float t) {  		
		if (mBounds.centerX() != x || mBounds.centerY() != y) {
			//              left                 top            right         bottom  
			mBounds.set(x + ((mWidth / 2) * -1), y + (mHeight / 2), x
					+ (mWidth / 2), y + ((mHeight / 2) * -1));
			//atualmente suporta apenas sheets com uma linha
			float[] tmp = {
					// X                 , Y     ,      S          , T
					x,
					y,
					s,
					t, //centro

					mBounds.left,
					mBounds.bottom,
					s - (mBounds.width() / 2),
					mBounds.height(), //inf. esq.

					mBounds.right, mBounds.bottom,
					s + (mBounds.width() / 2),
					mBounds.height(), //inf. dir.

					mBounds.right, mBounds.top, s + (mBounds.width() / 2),
					0f, //sup. dir.

					mBounds.left, mBounds.top, s - (mBounds.width() / 2),
					0f, //sup. esq.  

					mBounds.left, mBounds.bottom, s - (mBounds.width() / 2),
					mBounds.height() //inf. esq.

			};

			mVertexArray = new VertexArray(tmp);
		}

	}
	//------------------------------

	//--------COMPONENTS------------
	public Component getUpdatableComponent(int id){
		return mUpdatableComponents.get(id);
	}

	public Component getRenderableComponent(int id){
		return mRenderableComponents.get(id);
	}

	public Component getComponent(String name){

		for (Component component : mUpdatableComponents) {
			if(component.getName().equals(name)) return component;
		}
		for (Component component : mRenderableComponents) {
			if(component.getName().equals(name)) return component;
		}

		return new Component("NullComponent");
	}

	public ArrayList<Component> getComponents() {
		ArrayList<Component> components = new ArrayList<Component>();
		components.addAll(mUpdatableComponents);
		components.addAll(mRenderableComponents);
		return components;
	}

	public ArrayList<Component> getUpdatableComponents() {
		return mUpdatableComponents;
	}

	public void setUpdatableComponents(ArrayList<Component> updatableComponents) {
		this.mUpdatableComponents = updatableComponents;
	}

	public ArrayList<Component> getRenderableComponents() {
		return mRenderableComponents;
	}

	public void setRenderableComponents(ArrayList<Component> renderableComponents) {
		this.mRenderableComponents = renderableComponents;
	}

	public void addComponent(Component component){
		if(component instanceof Updatable) {
			mUpdatableComponents.add(component);
			component.setId(mUpdatableComponents.indexOf(component));
		}
		if(component instanceof Renderable) {
			mRenderableComponents.add(component);
			component.setId(mRenderableComponents.indexOf(component));
		}
	}
	//---------------------------

	//-------POSICIONAMENTO------
	public Vector2f getPosition() {
		return mPosition;
	}

	public void setPosition(Vector2f position) {
		this.mPosition = position;
	}

	public float getRotation() {
		return mRotation;
	}

	public void setRotation(float mRotation) {
		this.mRotation = mRotation;
	}

	public float getWidth() {
		return mWidth;
	}

	public void setWidth(float width) {
		this.mWidth = width;
	}

	public float getHeight() {
		return mHeight;
	}

	public void setHeight(float height) {
		this.mHeight = height;
	}

	public RectF getBounds() {
		return new RectF(mBounds.left - mColOffset.getX(), mBounds.top - mColOffset.getY(), mBounds.right + mColOffset.getX(), mBounds.bottom + mColOffset.getY());
		//		return mBounds;
	}

	public void setBounds(RectF bounds) {
		this.mBounds = bounds;
	}
	//-----------------------------

	public float getLayer() {
		return mLayer;
	}

	public void setLayer(float mLayer) {
		this.mLayer = mLayer;
	}

	//------MESSAGES---------------
	private void removeRecievedMessages(){
		mMessagesToRemove.clear();
		for (int i = 0; i < mCurMessages.size(); i++) {
			if (mCurMessages.get(i).hasBeenRecieved()) {
				mMessagesToRemove.add(mCurMessages.get(i));
			}
		}
		mCurMessages.removeAll(mMessagesToRemove);
	}

	public void recieveMessage(Message message) {
		mCurMessages.add(message);
	}

	public void recieveMessages(ArrayList<Message> messages) {
		mCurMessages = messages;
	}

	public Message getMessage(String operation) {

		for (Message message : mCurMessages) {
			if(message.getOperation().equals(operation)) {
				message.recieve();
				return message;
			}
		}

		return null;
	}

	public ArrayList<Message> getMessages() {
		return mCurMessages;
	}
	//-----------------------------

	public int getId() {
		return mId;
	}

	public void setId(int mId) {
		this.mId = mId;
	}

	public OBJ_TYPE getType() {
		return type;
	}

	public void setType(OBJ_TYPE type) {
		this.type = type;
	}

	public boolean isDead() {
		return isDead;
	}

	public void die() {
		isDead = true;
	}

}
