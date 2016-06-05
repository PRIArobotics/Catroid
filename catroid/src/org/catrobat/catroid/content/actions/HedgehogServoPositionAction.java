/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.content.actions;

import android.util.Log;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.devices.hedgehog.HedgehogService;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.InterpretationException;

public class HedgehogServoPositionAction extends TemporalAction {

	private static final String TAG = HedgehogServoPositionAction.class.getSimpleName();

	private Formula portFormula;
	private Formula positionFormula;

	private Sprite sprite;

	private int portInterpretation;
	private int positionInterpretation;

	@Override
	protected void begin() {
		try {
			portInterpretation = portFormula == null ? Integer.valueOf(0) : portFormula.interpretInteger(sprite);
		} catch (InterpretationException interpretationException) {
			portInterpretation = 0;
			Log.d(TAG, "Formula interpretation for this specific Brick failed. (port)",
					interpretationException);
		}

		try {
			positionInterpretation = positionFormula == null ? Integer.valueOf(0) : positionFormula
					.interpretInteger(sprite);
		} catch (InterpretationException interpretationException) {
			positionInterpretation = 0;
			Log.d(TAG, "Formula interpretation for this specific Brick failed. (position)",
					interpretationException);
		}
	}

	@Override
	protected void update(float percent) {
		Log.d(TAG, "Hedgehog servo port=" + portInterpretation + ", position=" + positionInterpretation);
		HedgehogService.getInstance().getClient().setServo(portInterpretation, true, positionInterpretation);
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void setPortFormula(Formula portFormula) {
		this.portFormula = portFormula;
	}

	public void setPositionFormula(Formula positionFormula) {
		this.positionFormula = positionFormula;
	}
}
