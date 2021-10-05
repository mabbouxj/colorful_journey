package net.mabbouxj.colorful_journey.compat.jei;

import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.helpers.IGuiHelper;

public class TickTimer implements ITickTimer {

    private final int maxValue;
    private final ITickTimer internalTimer;

    public TickTimer(IGuiHelper guiHelper, int maxValue) {
        this(guiHelper, 40, maxValue, true);
    }

    public TickTimer(IGuiHelper guiHelper, int maxValue, boolean countDown) {
        this(guiHelper, 40, maxValue, countDown);
    }

    public TickTimer(IGuiHelper guiHelper, int ticksPerCycle, int maxValue, boolean countDown) {
        this.maxValue = maxValue;
        this.internalTimer = guiHelper.createTickTimer(ticksPerCycle, maxValue, countDown);
    }

    @Override
    public int getValue() {
        return this.internalTimer.getValue() % maxValue;
    }

    @Override
    public int getMaxValue() {
        return maxValue;
    }

}
