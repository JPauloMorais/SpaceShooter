package br.jp.redsparrow.engine.core.components;

import br.jp.redsparrow.engine.core.GameObject;
import br.jp.redsparrow.engine.core.Updatable;

public abstract class StatsComponent extends Component implements Updatable {

	protected int mHealth;
	protected int mSpeed;
	
	public StatsComponent(GameObject parent, int health, int speed) {
		super("Stats", parent);
		
		mHealth = health;
		mSpeed = speed;
		
	}
	
	@Override
	public void update(GameObject object) {
		if(mHealth <= 0) die();
	}

	protected abstract void die();

	public int getSpeed() {
		return mSpeed;
	}

	public void setSpeed(int mSpeed) {
		this.mSpeed = mSpeed;
	}

	public int getHealth() {
		return mHealth;
	}

	public void setHealth(int mHealth) {
		this.mHealth = mHealth;
	}

}
