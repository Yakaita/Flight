/*
 * Flight
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.flight.gui.events;


import ca.tweetzy.flight.gui.Gui;
import ca.tweetzy.flight.gui.GuiManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiClickEvent extends GuiEvent {
    public final int slot;
    public final boolean guiClicked;
    public final ItemStack cursor, clickedItem;
    public final ClickType clickType;
    public final InventoryClickEvent event;

    public GuiClickEvent(GuiManager manager, Gui gui, Player player, InventoryClickEvent event, int slot, boolean guiClicked) {
        super(manager, gui, player);

        this.slot = slot;
        this.guiClicked = guiClicked;
        this.cursor = event.getCursor();

        Inventory clicked = event.getClickedInventory();

        this.clickedItem = clicked == null ? null : clicked.getItem(event.getSlot());
        this.clickType = event.getClick();
        this.event = event;
    }
}
