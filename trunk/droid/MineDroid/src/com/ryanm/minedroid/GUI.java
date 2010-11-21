
package com.ryanm.minedroid;

import static android.opengl.GLES10.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES10.glClear;

import com.ryanm.droid.rugl.Game;
import com.ryanm.droid.rugl.gl.GLUtil;
import com.ryanm.droid.rugl.gl.StackedRenderer;
import com.ryanm.droid.rugl.input.AbstractTouchStick;
import com.ryanm.droid.rugl.input.Touch;
import com.ryanm.droid.rugl.input.TouchStickArea;
import com.ryanm.droid.rugl.res.FontLoader;
import com.ryanm.droid.rugl.res.ResourceLoader;
import com.ryanm.droid.rugl.text.Font;
import com.ryanm.droid.rugl.text.TextShape;
import com.ryanm.droid.rugl.util.Colour;

/**
 * Holds the touchsticks
 * 
 * @author ryanm
 */
public class GUI
{
	private static final float radius = 50;

	private static final float padSize = 150;

	/***/
	public final AbstractTouchStick left = new TouchStickArea( 10, 10, padSize, padSize,
			radius );

	/***/
	public final AbstractTouchStick right = new TouchStickArea( 800 - padSize - 10, 10,
			padSize, padSize, radius );

	private StackedRenderer r = new StackedRenderer();

	private TextShape notification;

	private float notifyTime = 0;

	private Font font;

	/***/
	public GUI()
	{
		Touch.addListener( left );
		Touch.addListener( right );

		ResourceLoader.load( new FontLoader( com.ryanm.droid.rugl.R.raw.font, false ) {
			@Override
			public void fontLoaded()
			{
				font = resource;
			}
		} );
	}

	/**
	 * @param delta
	 */
	public void advance( float delta )
	{
		left.advance();
		right.advance();

		notifyTime -= delta;
		if( notifyTime < 0 )
		{
			notification = null;
		}
	}

	/**
	 * Note that the projection matrix will be changed and the depth
	 * buffer cleared in here
	 */
	public void draw()
	{
		GLUtil.scaledOrtho( 800, 480, Game.width, Game.height );
		Touch.setScreenSize( 800, 480, Game.width, Game.height );

		glClear( GL_DEPTH_BUFFER_BIT );

		left.draw( r );
		right.draw( r );

		if( notification != null )
		{
			notification.render( r );
		}

		r.render();
		r.clear();
	}

	/**
	 * @param string
	 */
	public void notify( String string )
	{
		if( font != null )
		{
			notification = font.buildTextShape( string, Colour.black );
			notification.translate(
					( Game.width - notification.getBounds().x.getSpan() ) / 2, 100, 0 );
			notifyTime = 1.5f;
		}
	}
}