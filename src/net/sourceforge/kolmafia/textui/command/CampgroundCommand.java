/**
 * Copyright (c) 2005-2020, KoLmafia development team
 * http://kolmafia.sourceforge.net/
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  [1] Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *  [2] Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in
 *      the documentation and/or other materials provided with the
 *      distribution.
 *  [3] Neither the name "KoLmafia" nor the names of its contributors may
 *      be used to endorse or promote products derived from this software
 *      without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION ) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE ) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package net.sourceforge.kolmafia.textui.command;

import net.sourceforge.kolmafia.KoLmafia;
import net.sourceforge.kolmafia.KoLCharacter;

import net.sourceforge.kolmafia.preferences.Preferences;

import net.sourceforge.kolmafia.request.CampAwayRequest;
import net.sourceforge.kolmafia.request.CampgroundRequest;
import net.sourceforge.kolmafia.request.ChateauRequest;
import net.sourceforge.kolmafia.request.FalloutShelterRequest;
import net.sourceforge.kolmafia.request.GenericRequest;

import net.sourceforge.kolmafia.session.Limitmode;

import net.sourceforge.kolmafia.utilities.StringUtilities;

public class CampgroundCommand
	extends AbstractCommand
{
	public CampgroundCommand()
	{
		this.usage = " rest | <etc.> [<numTimes>] - perform campground actions.";
	}

	@Override
	public void run( final String cmd, final String parameters )
	{
		String[] parameterList = parameters.split( "\\s+" );

		String command = parameterList[ 0 ];
		GenericRequest request = null;

		if ( command.equals( "rest" ) && ChateauRequest.chateauRestUsable() )
		{
			request = new ChateauRequest( "chateau_restbox" );
		}
		else if ( command.equals( "rest" ) && CampAwayRequest.campAwayTentRestUsable() )
		{
			request = new CampAwayRequest( CampAwayRequest.TENT );
		}
		else
		{
			if ( !Limitmode.limitCampground() && !KoLCharacter.isEd() )
			{
				if ( !KoLCharacter.inNuclearAutumn() )
				{
					request = new CampgroundRequest( command );
				}
				else
				{
					if ( command.equals( "rest" ) )
					{
						command = "vault1";
					}
					else if ( command.equals( "terminal" ) )
					{
						command = "vault_term";
					}
					request = new FalloutShelterRequest( command );
				}
			}
		}

		int count = 1;

		if ( parameterList.length > 1 )
		{
			if ( command.equals( "rest" ) && parameterList[ 1 ].equals( "free" ) )
			{
				count = Preferences.getInteger( "timesRested" ) >= KoLCharacter.freeRestsAvailable() ? 0 : 1;
			}
			else
			{
				count = StringUtilities.parseInt( parameterList[ 1 ] );
			}
		}

		KoLmafia.makeRequest( request, count );
	}
}