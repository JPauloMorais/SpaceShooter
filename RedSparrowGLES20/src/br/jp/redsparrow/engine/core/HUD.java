package br.jp.redsparrow.engine.core;

import java.util.ArrayList;

public class HUD {

	private static ArrayList<HUDitem> mHudItems;
	
	public static void init(){
		mHudItems = new ArrayList<HUDitem>();
	}
	
	public static void loop(float[] projectionMatrix){
		
		for (HUDitem hItem : mHudItems) {
			hItem.update(null);
		}
		
	}

}