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
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class HedgehogServoDeactivateBrick extends FormulaBrick {

    private static final long serialVersionUID = 1L;
    private transient View prototypeView;

    public HedgehogServoDeactivateBrick(int port) {
        initializeBrickFields(new Formula(port));
    }

    private void initializeBrickFields(Formula port) {
        addAllowedBrickField(BrickField.HEDGEHOG_SERVO_PORT);

        setFormulaWithBrickField(BrickField.HEDGEHOG_SERVO_PORT, port);
    }

    @Override
    public int getRequiredResources() {
        return HEDGEHOG
                | getFormulaWithBrickField(BrickField.HEDGEHOG_SERVO_PORT).getRequiredResources();
    }

    @Override
    public View getPrototypeView(Context context) {
        prototypeView = View.inflate(context, R.layout.brick_hedgehog_servo_deactivate, null);

        TextView textPort = (TextView) prototypeView.findViewById(R.id.brick_hedgehog_servo_port_prototype_text_view);
        textPort.setText(String.valueOf(BrickValues.HEDGEHOG_SERVO_INITIAL_PORT));

        return prototypeView;
    }

    @Override
    public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
        if (animationState) {
            return view;
        }
        if (view == null) {
            alphaValue = 255;
        }

        view = View.inflate(context, R.layout.brick_hedgehog_servo_deactivate, null);
        view = getViewWithAlpha(alphaValue);

        setCheckboxView(R.id.brick_hedgehog_servo_deactivate_checkbox);

        final Brick brickInstance = this;
        checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
                adapter.handleCheck(brickInstance, isChecked);
            }
        });

        TextView textPort = (TextView) view.findViewById(R.id.brick_hedgehog_servo_port_prototype_text_view);
        TextView editPort = (TextView) view.findViewById(R.id.brick_hedgehog_servo_port_edit_text);
        getFormulaWithBrickField(BrickField.HEDGEHOG_SERVO_PORT).setTextFieldId(R.id.brick_hedgehog_servo_port_edit_text);
        getFormulaWithBrickField(BrickField.HEDGEHOG_SERVO_PORT).refreshTextField(view);

        textPort.setVisibility(View.GONE);
        editPort.setVisibility(View.VISIBLE);
        editPort.setOnClickListener(this);

        return view;
    }

    @Override
    public View getViewWithAlpha(int alphaValue) {
        if (view != null) {
            View layout = view.findViewById(R.id.brick_hedgehog_servo_deactivate_layout);
            Drawable background = layout.getBackground();
            background.setAlpha(alphaValue);

            TextView textPort = (TextView) view.findViewById(R.id.brick_hedgehog_servo_port_prototype_text_view);
            TextView editPort = (TextView) view.findViewById(R.id.brick_hedgehog_servo_port_edit_text);

            textPort.setTextColor(textPort.getTextColors().withAlpha(alphaValue));
            editPort.setTextColor(editPort.getTextColors().withAlpha(alphaValue));
            editPort.getBackground().setAlpha(alphaValue);

            this.alphaValue = alphaValue;
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if (checkbox.getVisibility() == View.VISIBLE) {
            return;
        }

        switch (view.getId()) {
            case R.id.brick_hedgehog_servo_port_edit_text:
                FormulaEditorFragment.showFragment(view, this, BrickField.HEDGEHOG_SERVO_PORT);
                break;
        }
    }

    @Override
    public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
        sequence.addAction(sprite.getActionFactory().createHedgehogServoDeactivateAction(sprite,
                getFormulaWithBrickField(BrickField.HEDGEHOG_SERVO_PORT)));
        return null;
    }

    public void showFormulaEditorToEditFormula(View view) {
        FormulaEditorFragment.showFragment(view, this, BrickField.HEDGEHOG_SERVO_PORT);
    }

    @Override
    public void updateReferenceAfterMerge(Project into, Project from) {
    }
}
