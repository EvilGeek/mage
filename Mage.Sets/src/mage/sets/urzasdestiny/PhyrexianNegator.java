/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.urzasdestiny;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.dynamicvalue.common.StaticValue;
import mage.abilities.effects.common.SacrificeEffect;
import mage.abilities.keyword.TrampleAbility;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.filter.FilterPermanent;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.players.Player;
import mage.target.targetpointer.FixedTarget;
/**
 *
 * @author fireshoes
 */
public class PhyrexianNegator extends CardImpl {

    public PhyrexianNegator(UUID ownerId) {
        super(ownerId, 65, "Phyrexian Negator", Rarity.RARE, new CardType[]{CardType.CREATURE}, "{2}{B}");
        this.expansionSetCode = "UDS";
        this.subtype.add("Horror");

        this.power = new MageInt(5);
        this.toughness = new MageInt(5);

        // Trample
        this.addAbility(TrampleAbility.getInstance());
        // Whenever Phyrexian Negator is dealt damage, sacrifice that many permanents.
        this.addAbility(new PhyrexianNegatorTriggeredAbility());
    }

    public PhyrexianNegator(final PhyrexianNegator card) {
        super(card);
    }

    @Override
    public PhyrexianNegator copy() {
        return new PhyrexianNegator(this);
    }
}

class PhyrexianNegatorTriggeredAbility extends TriggeredAbilityImpl {
    PhyrexianNegatorTriggeredAbility() {
        super(Zone.BATTLEFIELD, new SacrificeEffect(new FilterPermanent(), 0,""));
    }

    PhyrexianNegatorTriggeredAbility(final PhyrexianNegatorTriggeredAbility ability) {
        super(ability);
    }
    
    @Override
    public PhyrexianNegatorTriggeredAbility copy() {
        return new PhyrexianNegatorTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.DAMAGED_CREATURE;
    }
    
    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (event.getTargetId().equals(this.sourceId)) {
            UUID controller = game.getControllerId(event.getTargetId());
            if (controller != null) {
                Player player = game.getPlayer(controller);
                if (player != null) {
                    getEffects().get(0).setTargetPointer(new FixedTarget(player.getId()));
                    ((SacrificeEffect) getEffects().get(0)).setAmount(new StaticValue(event.getAmount()));
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public String getRule() {
        return "Whenever {this} is dealt damage, sacrifice that many permanents.";
    }
}