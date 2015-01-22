package br.jp.redsparrow.game.components;

import br.jp.redsparrow.engine.core.GameObject;
import br.jp.redsparrow.engine.core.Vector2f;
import br.jp.redsparrow.engine.core.components.PhysicsComponent;
import br.jp.redsparrow.game.ObjectFactory.OBJECT_TYPE;

public class ProjectilePhysicsComponent extends PhysicsComponent {

	private OBJECT_TYPE mShooterType;
	private boolean shot = false;

	private Vector2f mVelocity = new Vector2f(0f, 0f);
	//	private Vector2f location = new Vector2f(0f, 0f); 

	private boolean hitTarget;

	public ProjectilePhysicsComponent(GameObject parent) {
		super(parent);

		hitTarget = false;
		mMaxVels = new Vector2f(parent.getWidth()/2, parent.getHeight()/2);

	}

	@Override
	public void update(GameObject parent) {			

		if (!hitTarget) {
			super.update(parent);
			if (!shot) {
				//Input de Movimentacao
				try {

					mVelocity = (Vector2f) parent.getMessage("MOVE").getMessage();
					shot = true;

				} catch (Exception e) {
				}

			}

			parent.setPosition(	parent.getPosition().add(mVelocity) );

		}else parent.die();
		//			((LifeComponent) parent.getUpdatableComponent(1)).die();
	}

	public boolean wasShot() {
		return shot;
	}

	public void shoot(Vector2f velocity) {
		mVelocity = velocity;
		mParent.setRotation(Math.atan2(velocity.getY(), velocity.getX()) - 1.5707963268d);
		shot = true;
	}

	public boolean hasHitTarget() {
		return hitTarget;
	}

	public void hitTarget() {
		this.hitTarget = true;
	}

	public OBJECT_TYPE getShootertype() {
		return mShooterType;
	}

	public void setShooterType(OBJECT_TYPE shooterType) {
		this.mShooterType = shooterType;
	}

}