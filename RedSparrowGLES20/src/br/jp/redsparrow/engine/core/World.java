package br.jp.redsparrow.engine.core;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;
import br.jp.redsparrow.R;
import br.jp.redsparrow.engine.core.components.PhysicsComponent;
import br.jp.redsparrow.engine.core.components.ProjectilePhysicsComponent;
import br.jp.redsparrow.engine.core.components.SoundComponent;
import br.jp.redsparrow.engine.core.messages.Message;
import br.jp.redsparrow.engine.core.util.LogConfig;
import br.jp.redsparrow.game.ObjectFactory.OBJ_TYPE;

public class World implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//TODO: Saveinstancestate stuff
	private static final String TAG = "World";
	private static boolean isRunning;

	private static GameObject mPlayer;
	private static ArrayList<GameObject> mGameObjects;
	private static ArrayList<GameObject> mToRemove;
	private static ArrayList<GameObject> mToCheck;
	private static Quadtree mQuadTree;

	private static final float mUPDATE_RANGE_X = 16.0f;
	private static final float mUPDATE_RANGE_Y = 20.0f;
	private static final float mRENDERING_RANGE_X = 10.0f;
	private static final float mRENDERING_RANGE_Y = 16.0f;

	private static SoundComponent bgmSoundComponent;
	private static float bgMusicRightVol = 0.05f;
	private static float bgMusicLeftVol = 0.05f;

	public static void init(Context context){

		isRunning = false;

		mPlayer = new GameObject();
		mGameObjects = new ArrayList<GameObject>();
		mToRemove = new ArrayList<GameObject>();
		mToCheck = new ArrayList<GameObject>();
		mQuadTree = new Quadtree(0, new RectF(-100, -100, 100, 100));

		bgmSoundComponent = new SoundComponent(context);
		bgmSoundComponent.addSound(R.raw.at_least_you_tried_greaf);
		bgmSoundComponent.addSound(R.raw.test_shot);

	}

	private static void onStart(){
		isRunning = true;
		//		physicsCheckT.start();
		float targetVol[] = { bgMusicLeftVol, bgMusicRightVol };
		bgmSoundComponent.fadeIn(0, targetVol, 1, true);
	}


	public static void loop(float[] projectionMatrix){
		if (isRunning) {
			//-------LIMPANDO---------
			mQuadTree.clear();
			removeDead();

			//------PREECHENDO QUADTREE-------
			for (int k=0; k < mGameObjects.size(); k++) {
				if(mGameObjects.get(k).getPosition().getX() < getPlayer().getPosition().getX()+mUPDATE_RANGE_X &&
						mGameObjects.get(k).getPosition().getX() > getPlayer().getPosition().getX()-mUPDATE_RANGE_X &&
						mGameObjects.get(k).getPosition().getY() < getPlayer().getPosition().getY()+mUPDATE_RANGE_Y &&
						mGameObjects.get(k).getPosition().getY() > getPlayer().getPosition().getY()-mUPDATE_RANGE_Y)
				{
					mQuadTree.add(mGameObjects.get(k));
				}
			}
			
			//------LOOP DOS OBJETOS-----------
			if(mGameObjects!=null && !mGameObjects.isEmpty()){
				for (int i=0; i < mGameObjects.size(); i++) {
//					mToCheck.clear();
//					mQuadTree.getToCheck(mToCheck, mGameObjects.get(i).getBounds());

					//CHECANDO COLISAO
//					for (int j=0; j < mToCheck.size(); j++) {
//						if(Collision.areIntersecting( mGameObjects.get(i).getBounds(), mToCheck.get(j).getBounds()) ){
//
//							try {
//								PhysicsComponent pc = ((PhysicsComponent) mToCheck.get(i).getUpdatableComponent(0));
//								float[] Tvels = {
//										pc.getVelX(), pc.getVelY()
//								};
//								pc = ((PhysicsComponent) mToCheck.get(j).getUpdatableComponent(0));
//								float[] Ovels = {
//										pc.getVelX(), pc.getVelY()
//								};
//								mToCheck.get(j).recieveMessage(new Message(mToCheck.get(j).getId(), "Collision", Ovels));
//
//							} catch (Exception e) {
//
//								try {
//									if(((ProjectilePhysicsComponent) mToCheck.get(j).getUpdatableComponent(0)).getShooterType()!=mGameObjects.get(i).getType())
//									{
//										mToCheck.get(i).die();
//										mToCheck.get(j).die();
//									}
//								} catch (Exception e1) {
//									// TODO Auto-generated catch block
//									e1.printStackTrace();
//								}
//
//							}
//						}
//					}

					//Update e Render se dentro limite
					if(mGameObjects.get(i).getPosition().getX() < getPlayer().getPosition().getX()+mUPDATE_RANGE_X &&
							mGameObjects.get(i).getPosition().getX() > getPlayer().getPosition().getX()-mUPDATE_RANGE_X &&
							mGameObjects.get(i).getPosition().getY() < getPlayer().getPosition().getY()+mUPDATE_RANGE_Y &&
							mGameObjects.get(i).getPosition().getY() > getPlayer().getPosition().getY()-mUPDATE_RANGE_Y)
					{
						mGameObjects.get(i).update();
					}

					if(mGameObjects.get(i).getPosition().getX() < getPlayer().getPosition().getX()+mRENDERING_RANGE_X &&
							mGameObjects.get(i).getPosition().getX() > getPlayer().getPosition().getX()-mRENDERING_RANGE_X &&
							mGameObjects.get(i).getPosition().getY() < getPlayer().getPosition().getY()+mRENDERING_RANGE_Y &&
							mGameObjects.get(i).getPosition().getY() > getPlayer().getPosition().getY()-mRENDERING_RANGE_Y)
					{
						mGameObjects.get(i).render(projectionMatrix);
					}

				}
			}

			//------LOOP DO PLAYER-------------
			mToCheck.clear();
			mQuadTree.getToCheck(mToCheck, mPlayer.getBounds());
			
			for (int i = 0; i < mToCheck.size(); i++) {
				if(Collision.areIntersecting(mPlayer.getBounds(), mToCheck.get(i).getBounds())){
					try {

						PhysicsComponent pc = ((PhysicsComponent) mPlayer.getUpdatableComponent(0));
						float[] Pvels = {
								pc.getVelX(), pc.getVelY()
						};

						pc = ((PhysicsComponent) mToCheck.get(i).getUpdatableComponent(0));
						float[] Ovels = {
								pc.getVelX(), pc.getVelY()
						};

						mPlayer.recieveMessage(new Message(-2, "Collision", Ovels));
						mToCheck.get(i).recieveMessage(new Message(-2, "Collision", Pvels));

					} catch (Exception e) { 

						if(((ProjectilePhysicsComponent) mToCheck.get(i).getUpdatableComponent(0)).getShooterType()!=OBJ_TYPE.PLAYER)
							mPlayer.die();
							mToCheck.get(i).die();
					}

				}
			}
			mPlayer.update();
			mPlayer.render(projectionMatrix);

		}else {
			onStart();
		}
	}

	public static void pause(){
		try {
			bgmSoundComponent.pauseSound(0);
		} catch (Exception e) {

		}
	}

	public static void resume(){
		//		bgMusic.start();
	}

	public static void stop(){
		try {
			bgmSoundComponent.stopSound(0);
			bgmSoundComponent.releaseSound(0);
		} catch (Exception e) {

		}
	}

	public static SoundComponent getBgmSoundComponent() {
		return bgmSoundComponent;
	}

	public static void setBgmSoundComponent(SoundComponent bgmSoundComponent) {
		World.bgmSoundComponent = bgmSoundComponent;
	}

	public static GameObject getPlayer() {
		if(mPlayer!=null) return mPlayer;
		else return new GameObject();
	}

	public static void setPlayer(GameObject mPlayer) {
		World.mPlayer = mPlayer;
	}

	public static GameObject getObject(int index){

		return mGameObjects.get(index);

	}

	public static GameObject getObject(GameObject object){

		return mGameObjects.get(mGameObjects.indexOf(object));

	}

	public static GameObject getObjectById(int id){

		if(mGameObjects != null){			
			for (GameObject gameObject : mGameObjects) {

				if(gameObject.getId()==id) return mGameObjects.get(mGameObjects.indexOf(gameObject));

			}
		}

		return null;

	}

	public static void addObject(GameObject object){

		//TODO: sistema eficiente de atribuicao de ids
		//Se id ja nao foi estabelecido, atribuir baseado em posicao no array


		if(object.getId() == -2) { 
			object.setId(mGameObjects.size()-1);
		}

		mGameObjects.add(object);

		if(LogConfig.ON) Log.i(TAG, "Objeto de id " + object.getId() + " add em " + object.getPosition().toString());
	}

	public static void addObjects(GameObject ... objects){
		for (int i = 0; i < objects.length; i++) {
			World.addObject(objects[i]);
		}
	}

	private static void removeDead(){
		mToRemove.clear();
		for (int i = 0; i < mGameObjects.size(); i++) {
			if(mGameObjects.get(i).isDead()) mToRemove.add(mGameObjects.get(i));
		}
		if(LogConfig.ON && mToRemove.size() > 0) Log.i(TAG, mToRemove.size() + " objeto(s) morto(s) removido(s)");
		mGameObjects.removeAll(mToRemove);
	}

	public static void removeObject(int index){
		mGameObjects.remove(index);
	}

	public static void removeObject(GameObject object){
		mGameObjects.remove(object);
	}

	public static boolean isRunning() {
		return isRunning;
	}

	public static void setRunning(boolean isRunning) {
		World.isRunning = isRunning;
	}

	public static void sendMessages(final int objectId, final ArrayList<Message> curMessages) {
		try {
			getObject(objectId).recieveMessages(curMessages);
		} catch (Exception e) {	}
	}
}



