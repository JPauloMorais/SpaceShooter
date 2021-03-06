package br.jp.redsparrow_zsdemo.game;

import java.util.ArrayList;

import br.jp.redsparrow_zsdemo.R;
import br.jp.redsparrow_zsdemo.engine.Animation;
import br.jp.redsparrow_zsdemo.engine.GameObject;
import br.jp.redsparrow_zsdemo.engine.Vector2f;
import br.jp.redsparrow_zsdemo.engine.components.AnimatedSpriteComponent;
import br.jp.redsparrow_zsdemo.engine.components.SoundComponent;
import br.jp.redsparrow_zsdemo.engine.components.SpriteComponent;
import br.jp.redsparrow_zsdemo.engine.game.Game;
import br.jp.redsparrow_zsdemo.engine.game.ObjectFactory;
import br.jp.redsparrow_zsdemo.engine.game.ObjectType;
import br.jp.redsparrow_zsdemo.engine.physics.AABB;
import br.jp.redsparrow_zsdemo.game.objecttypes.basicenemy.EnemyAIComponent;
import br.jp.redsparrow_zsdemo.game.objecttypes.basicenemy.EnemyGunComponent;
import br.jp.redsparrow_zsdemo.game.objecttypes.basicenemy.EnemyPhysicsComponent;
import br.jp.redsparrow_zsdemo.game.objecttypes.basicenemy.EnemyProjectilePhysicsComponent;
import br.jp.redsparrow_zsdemo.game.objecttypes.basicenemy.EnemyStatsComponent;
import br.jp.redsparrow_zsdemo.game.objecttypes.basicplayer.PlayerGunComponent;
import br.jp.redsparrow_zsdemo.game.objecttypes.basicplayer.PlayerPhysicsComponent;
import br.jp.redsparrow_zsdemo.game.objecttypes.basicplayer.PlayerStatsComponent;
import br.jp.redsparrow_zsdemo.game.objecttypes.spawnpoint.SpawnPointComponent;

public class ReSpObjectFactory extends ObjectFactory {

	private ArrayList<Integer> typeCounts;
	
	public ReSpObjectFactory(Game game) {
		super(game);

		typeCounts = new ArrayList<Integer>();
		
		//-----SUPERTYPES-----------
		mSupertypes.add(new ObjectType("Player", null) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				return null;
			}
		});
		mSupertypes.add(new ObjectType("Enemy", null) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				return null;
			}
		});
		mSupertypes.add(new ObjectType("Projectile", null) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				return null;
			}
		});
		mSupertypes.add(new ObjectType("BG", null) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				return null;
			}
		});
		mSupertypes.add(new ObjectType("Default", null) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				return null;
			}
		});


		//------TYPES------------
		ObjectType basicPlayer = new ObjectType("BasicPlayer", mSupertypes.get(0)) {

			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), 1f, 1f));

				obj.setType(this);

				obj.addComponent( "Physics", new PlayerPhysicsComponent(obj));

				Animation animP = new Animation(5, 2);
				obj.addComponent("AnimatedSprite",
						new AnimatedSpriteComponent(game.getContext(), R.drawable.red_sparrow_test_2, obj,
								animP, 0.3f, 0.3f));
				((AnimatedSpriteComponent) obj.getRenderableComponent("AnimatedSprite")).addAnimation(game.getContext(),
						R.drawable.alpha_pixel, new Animation(10, 10));

				obj.addComponent("Sound",
						new SoundComponent(game.getContext(), obj, R.raw.test_shot_hit));
				((SoundComponent) obj.getUpdatableComponent("Sound"))
				.setSoundVolume(0, 0.01f, 0.01f);
				((SoundComponent) obj.getUpdatableComponent("Sound")).addSound(R.raw.test_explosion);

				obj.addComponent("Gun",
						new PlayerGunComponent(obj));

				obj.addComponent("Stats",
						new PlayerStatsComponent(obj, 10));
				
//				typeCounts.set(0, typeCounts.get(0) +1);
				
				return obj;
			}

		};
		mTypes.put(basicPlayer.getName(), basicPlayer);

		ObjectType basicEnemy1 = new ObjectType("BasicEnemy1", mSupertypes.get(1)) {

			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), 1f, 1f));

				obj.setType(this);

				obj.addComponent("Physics", new EnemyPhysicsComponent(obj));

				Animation anim = new Animation(1, 1);
				anim.setAmmoToWait(4);
				obj.addComponent("AnimatedSprite", new AnimatedSpriteComponent(game.getContext(), R.drawable.nave_f, obj,
						anim, 0.3f, 0.3f));
				((AnimatedSpriteComponent) obj.getRenderableComponent("AnimatedSprite")).addAnimation( game.getContext(), R.drawable.explosion_test,new Animation(5, 4));

				obj.addComponent("Sound",
						new SoundComponent(game.getContext(), obj, R.raw.test_explosion));
				((SoundComponent) obj.getUpdatableComponent("Sound"))
				.setSoundVolume(0, 0.005f, 0.005f);

				obj.addComponent("Gun", new EnemyGunComponent(obj, 0, 0));

				obj.addComponent("Stats", new EnemyStatsComponent(obj, 5));		

				obj.addComponent("AI", new EnemyAIComponent(obj));

//				typeCounts.set(1, typeCounts.get(1) +1);
				
				return obj;
			}

		};
		mTypes.put(basicEnemy1.getName(), basicEnemy1);

		ObjectType basicEnemy2 = new ObjectType("BasicEnemy2", mSupertypes.get(1)) {

			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), 1.5f, 1.5f));

				obj.setType(this);

				obj.addComponent("Physics", new EnemyPhysicsComponent(obj));

				Animation anim = new Animation(1, 1);
				anim.setAmmoToWait(4);
				obj.addComponent("AnimatedSprite", new AnimatedSpriteComponent(game.getContext(), R.drawable.basic_enemy_ship_dark, obj,
						anim, 0.3f, 0.3f));
				((AnimatedSpriteComponent) obj.getRenderableComponent("AnimatedSprite")).addAnimation( game.getContext(), R.drawable.explosion_test,new Animation(5, 4));

				obj.addComponent("Sound",
						new SoundComponent(game.getContext(), obj, R.raw.test_explosion));
				((SoundComponent) obj.getUpdatableComponent("Sound"))
				.setSoundVolume(0, 0.005f, 0.005f);

				obj.addComponent("Gun", new EnemyGunComponent(obj, 0, 0));

				obj.addComponent("Stats", new EnemyStatsComponent(obj, 15));		
				
				obj.addComponent("AI", new EnemyAIComponent(obj));

//				typeCounts.set(2, typeCounts.get(2) + 1);

				return obj;
			}

		};
		mTypes.put( basicEnemy2.getName(), basicEnemy2);

		ObjectType basicEnemy3 = new ObjectType("BasicEnemy3", mSupertypes.get(1)) {

			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), 2, 2));

				obj.setType(this);

				obj.addComponent("Physics", new EnemyPhysicsComponent(obj));

				Animation anim = new Animation(1, 1);
				anim.setAmmoToWait(4);
				obj.addComponent("AnimatedSprite", new AnimatedSpriteComponent(game.getContext(), R.drawable.nave_f_b, obj,
						anim, 0.6f, 0.6f));
				((AnimatedSpriteComponent) obj.getRenderableComponent("AnimatedSprite")).addAnimation(game.getContext(), R.drawable.explosion_test, new Animation(5, 4));

				obj.addComponent("Sound",
						new SoundComponent(game.getContext(), obj, R.raw.test_explosion));
				((SoundComponent) obj.getUpdatableComponent("Sound"))
				.setSoundVolume(0, 0.005f, 0.005f);

				obj.addComponent("Gun", new EnemyGunComponent(obj, 0, 0));

				obj.addComponent("Stats", new EnemyStatsComponent(obj, 20));

				obj.addComponent("AI", new EnemyAIComponent(obj));

//				typeCounts.set(3, typeCounts.get(3) +1);

				return obj;
			}

		};
		mTypes.put( basicEnemy3.getName(), basicEnemy3);

		ObjectType basicEnemy4 = new ObjectType("BasicEnemy4", mSupertypes.get(1)) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), 2.5f, 2.5f));

				obj.setType(this);

				obj.addComponent("Physics", new EnemyPhysicsComponent(obj));

				Animation anim = new Animation(12, 1);
				anim.setAmmoToWait(4);
				obj.addComponent("AnimatedSprite", new AnimatedSpriteComponent(game.getContext(), R.drawable.eyes_test, obj,
						anim, 0.5f, 0.5f));
				((AnimatedSpriteComponent) obj.getRenderableComponent("AnimatedSprite")).addAnimation( game.getContext(), R.drawable.explosion_test,new Animation(5, 4));

				obj.addComponent("Sound",
						new SoundComponent(game.getContext(), obj, R.raw.test_explosion));
				((SoundComponent) obj.getUpdatableComponent("Sound"))
						.setSoundVolume(0, 0.005f, 0.005f);

				obj.addComponent("Gun", new EnemyGunComponent(obj, 0, 0));

				obj.addComponent("Stats", new EnemyStatsComponent(obj, 25));

				obj.addComponent("AI", new EnemyAIComponent(obj));

//				typeCounts.set(2, typeCounts.get(2) + 1);

				return obj;
			}

		};
		mTypes.put( basicEnemy4.getName(), basicEnemy4);

		ObjectType basicProjectile = new ObjectType("BasicProjectile", mSupertypes.get(2)) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), .08f, .08f));

				obj.setType(this);

				obj.addComponent("Physics", new EnemyProjectilePhysicsComponent(obj,1));

				obj.addComponent("Sprite", new SpriteComponent(game.getContext(), R.drawable.player_projectile_1, obj, 0.12f,0.12f,
						2, 1, 0, 1));

//				typeCounts.set(4, typeCounts.get(4) +1);

				return obj;
			}

		};
		mTypes.put(basicProjectile.getName(), basicProjectile);


		ObjectType basicEnemyProjectile = new ObjectType("BasicEnemyProjectile", mSupertypes.get(2)) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), .08f, .08f));

				obj.setType(this);

				obj.addComponent("Physics", new EnemyProjectilePhysicsComponent(obj,1));

				obj.addComponent("Sprite", new SpriteComponent(game.getContext(), R.drawable.enemy_projectile_3, obj, 0.12f,0.12f,
						2, 1, 0, 1));

//				typeCounts.set(5, typeCounts.get(5) +1);

				return obj;
			}

		};
		mTypes.put(basicEnemyProjectile.getName(), basicEnemyProjectile);
		
		ObjectType basicProjectile2 = new ObjectType("BasicProjectile2", mSupertypes.get(2)) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), .1f, .1f));

				obj.setType(this);

				obj.addComponent("Physics", new EnemyProjectilePhysicsComponent(obj, 2));

				obj.addComponent("Sprite", new SpriteComponent(game.getContext(), R.drawable.player_projectile_2, obj, 0.12f,0.12f));

//				typeCounts.set(5, typeCounts.get(5) +1);

				return obj;
			}

		};
		mTypes.put(basicProjectile2.getName(), basicProjectile2);
		
		ObjectType basicProjectile3 = new ObjectType("BasicProjectile3", mSupertypes.get(2)) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), .15f, .15f));

				obj.setType(this);

				obj.addComponent("Physics", new EnemyProjectilePhysicsComponent(obj, 3));

				obj.addComponent("Sprite", new SpriteComponent(game.getContext(), R.drawable.player_projectile_3, obj, 0.12f,0.12f));

//				typeCounts.set(5, typeCounts.get(5) +1);

				return obj;
			}

		};
		mTypes.put(basicProjectile3.getName(), basicProjectile3);
		
		ObjectType basicProjectile4 = new ObjectType("BasicProjectile4", mSupertypes.get(2)) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), .2f, .2f));

				obj.setType(this);

				obj.addComponent("Physics", new EnemyProjectilePhysicsComponent(obj, 4));

				obj.addComponent("Sprite", new SpriteComponent(game.getContext(), R.drawable.player_projectile_4, obj, 0.12f,0.12f));

//				typeCounts.set(5, typeCounts.get(5) +1);

				return obj;
			}
		};
		mTypes.put(basicProjectile4.getName(), basicProjectile4);
		
		ObjectType basicProjectile5 = new ObjectType("BasicProjectile5", mSupertypes.get(2)) {
			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), .25f, .25f));

				obj.setType(this);

				obj.addComponent("Physics", new EnemyProjectilePhysicsComponent(obj, 5));

				obj.addComponent("Sprite", new SpriteComponent(game.getContext(), R.drawable.player_projectile_5, obj, 0.12f,0.12f));

//				typeCounts.set(5, typeCounts.get(5) +1);

				return obj;
			}

		};
		mTypes.put(basicProjectile5.getName(), basicProjectile5);

		ObjectType bg1 = new ObjectType("BG1", mSupertypes.get(3)) {

			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), 200, 200));

				obj.setType(this);

				obj.addComponent("Sprite", new SpriteComponent(game.getContext(), R.drawable.stars_test4, obj, 0, 0));

				return obj;
			}

		};
		mTypes.put( bg1.getName(), bg1);

		ObjectType bg2 = new ObjectType("BG2", mSupertypes.get(3)) {

			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), 200, 200));

				obj.setType(this);

				obj.addComponent("Sprite", new SpriteComponent(game.getContext(), R.drawable.stars_test4, obj, 0, 0));

				return obj;
			}

		};
		mTypes.put(bg2.getName(), bg2);

		ObjectType spawnPoint = new ObjectType("Spawnpoint", mSupertypes.get(4)) {

			@Override
			public GameObject create(Game game, float positionX, float positionY) {
				GameObject obj = new GameObject(new AABB(new Vector2f(positionX, positionY), 0, 0));

				obj.setType(this);
				
				obj.addComponent("Spawn", new SpawnPointComponent(obj,
						300, 2, "BasicEnemy1", "BasicEnemy2", "BasicEnemy3", "BasicEnemy4"));
				
				return obj;
			}
		};
		mTypes.put(spawnPoint.getName(), spawnPoint);

		
		for (int i = 0; i < mTypes.size(); i++) {
			typeCounts.add(0);
		}
	}

	public ArrayList<Integer> getTypeCounts() {
		return typeCounts;
	}

	public void setTypeCounts(ArrayList<Integer> typeCounts) {
		this.typeCounts = typeCounts;
	}

	@Override
	public GameObject create(String typeName, float x, float y) {

		if(mTypes.containsKey(typeName)) return mTypes.get(typeName).create(game, x, y);
		else return new GameObject(new AABB(new Vector2f(x, y), 0, 0));

	}

}
