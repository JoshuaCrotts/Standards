/*
===========================================================================
                   Standards Java Game Library Source Code
           Copyright (C) 2017-2019 Joshua Crotts & Andrew Matzureff 
Standards is free software: you can redistribute it and/or modify it under 
the terms of the GNU General Public License as published by the Free Software 
Foundation, either version 3 of the License, or (at your option) any later 
version.

Standards Source Code is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Standards Source Code. If not, see <http://www.gnu.org/licenses/>.

Standards is the long-overdue update to the everlasting Standards 2.0 library
Andrew Matzureff and I created two years ago. I am including it in this project
to simplify the rendering and logic pipeline, but with a focus on the MVC
paradigm.

We connect to the Apache FastMath API for some of our trigonometric functions,
and we use John Carmack's fast inverse square root function.
===========================================================================
 */
package com.revivedstandards.test;

import com.revivedstandards.controller.StandardAudioController;
import com.revivedstandards.handlers.StandardCollisionHandler;
import com.revivedstandards.handlers.StandardHandler;
import com.revivedstandards.main.StandardCamera;
import com.revivedstandards.main.StandardDraw;
import com.revivedstandards.main.StandardGame;
import com.revivedstandards.model.StandardID;
import com.revivedstandards.model.StandardLevel;
import java.awt.Color;

public class FollowTheMouseGameTest extends StandardGame
{

    private final TriangleGameObject tri;       //Player    
    private final StandardCamera sc;            //Camera
    private final StandardCollisionHandler sch; //Collision handler
    private final StandardLevel level;          //Level

    public FollowTheMouseGameTest ()
    {
        super( 1280, 720, "Game Test" );

        //  Initializes the audio control buffer
        StandardAudioController.init( 16 );

        //  Create a new collision handler
        this.sch = new StandardCollisionHandler( null );

        //  Instantiates a new TGO (the player)
        this.tri = new TriangleGameObject( this, this.sch, 200, 200, StandardID.Player );

        //  Instantiate the camera
        this.sc = new StandardCamera( this.tri, 1, this.getGameWidth(), this.getGameHeight() );

        //  Sets the player's camera to the global camera
        this.tri.setCamera( this.sc );
        //  Sets the collision handler's camera to the global camera
        this.sch.setCamera( this.sc );

        //  Add the player to the collision handler
        this.sch.addEntity( this.tri );

        //  Instantiates the level
        this.level = new SpaceLevel( tri );

        //  Spawn bricks
        this.spawnBricks( 10, 64 );

        for ( int i = 0 ; i < 10 ; i++ )
        {
            StandardAudioController.load( "src/res/audio/sfx/laser_sfx.wav" );
            StandardAudioController.load( "src/res/audio/sfx/damage_0.wav" );
            StandardAudioController.load( "src/res/audio/sfx/damage_1.wav" );
            StandardAudioController.load( "src/res/audio/sfx/damage_2.wav" );
        }
    }

    @Override
    public void tick ()
    {
        StandardHandler.Handler( this.sch );
        StandardHandler.Object( this.sc );
    }

    @Override
    public void render ()
    {
        this.level.render( StandardDraw.Renderer );
        StandardDraw.Object( this.sc );
        StandardDraw.Handler( this.sch );
    }

    private void spawnBricks ( int n, int dim )
    {
        for ( int y = 64 ; y <= n * 64 ; y += dim )
        {
            this.sch.addEntity( new BrickGameObject( this, this.sch, 100, y, dim, Color.ORANGE ) );
        }
    }

    public static void main ( String[] args )
    {
        FollowTheMouseGameTest gameTest = new FollowTheMouseGameTest();
        gameTest.StartGame();
    }
}
