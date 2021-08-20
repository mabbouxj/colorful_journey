package net.mabbouxj.colorful_journey.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.mabbouxj.colorful_journey.entities.ColoredChickenEntity;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredChickenModel<T extends ColoredChickenEntity> extends ChickenModel<T> {

    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer leg0;
    private final ModelRenderer leg1;
    private final ModelRenderer wing0;
    private final ModelRenderer wing1;
    private final ModelRenderer beak;
    private final ModelRenderer redThing;

    public ColoredChickenModel() {
        texWidth = 64;
        texHeight = 32;

        head = new ModelRenderer(this);
        head.setPos(0.0F, 15.0F, -4.0F);
        head.texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 6.0F, 3.0F, 0.0F, true);

        body = new ModelRenderer(this);
        body.setPos(0.0F, 16.0F, 0.0F);
        body.texOffs(0, 9).addBox(-3.0F, -3.0F, -4.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);

        leg0 = new ModelRenderer(this);
        leg0.setPos(-1.0F, 19.0F, 1.0F);
        leg0.texOffs(26, 0).addBox(1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        leg1 = new ModelRenderer(this);
        leg1.setPos(2.0F, 19.0F, 1.0F);
        leg1.texOffs(26, 0).addBox(-5.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, 0.0F, true);

        wing0 = new ModelRenderer(this);
        wing0.setPos(3.0F, 13.0F, 0.0F);
        wing0.texOffs(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, 0.0F, false);

        wing1 = new ModelRenderer(this);
        wing1.setPos(-3.0F, 13.0F, 0.0F);
        wing1.texOffs(24, 13).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F, 0.0F, true);

        beak = new ModelRenderer(this);
        beak.setPos(0.0F, 15.0F, -4.0F);
        beak.texOffs(14, 0).addBox(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

        redThing = new ModelRenderer(this);
        redThing.setPos(0.0F, 15.0F, -4.0F);
        redThing.texOffs(14, 4).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 2.0F, 0.0F, true);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.beak.xRot = this.head.xRot;
        this.beak.yRot = this.head.yRot;
        this.redThing.xRot = this.head.xRot;
        this.redThing.yRot = this.head.yRot;
        this.leg0.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg1.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.wing0.zRot = ageInTicks;
        this.wing1.zRot = -ageInTicks;
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public void prepareMobModel(T p_212843_1_, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
        super.prepareMobModel(p_212843_1_, p_212843_2_, p_212843_3_, p_212843_4_);
    }

    @Override
    protected Iterable<ModelRenderer> headParts() {
        return ImmutableList.of(this.head, this.beak, this.redThing);
    }

    @Override
    protected Iterable<ModelRenderer> bodyParts() {
        return ImmutableList.of(this.body, this.leg0, this.leg1, this.wing0, this.wing1);
    }
}
