package net.mabbouxj.colorful_journey.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EaselModel extends Model {
    public final ModelRenderer structure;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    public final ModelRenderer slate;
    private final ModelRenderer cube_r4;
    public final ModelRenderer paper;
    private final ModelRenderer cube_r5;

    public EaselModel() {
        super(RenderType::entityCutoutNoCull);
        texWidth = 32;
        texHeight = 32;

        structure = new ModelRenderer(this);
        structure.setPos(0.0F, 24.0F, 0.0F);
        structure.texOffs(0, 0).addBox(-4.001F, -22.0F, 4.001F, 1.0F, 22.0F, 1.0F, 0.0F, false);
        structure.texOffs(0, 0).addBox(3.001F, -22.0F, 4.001F, 1.0F, 22.0F, 1.0F, 0.0F, false);
        structure.texOffs(0, 0).addBox(-5.0F, -23.0F, 4.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
        structure.texOffs(0, 0).addBox(-7.0F, -9.0F, -3.0F, 14.0F, 1.0F, 2.0F, 0.0F, false);
        structure.texOffs(0, 0).addBox(-7.0F, -10.0F, -4.0F, 14.0F, 2.0F, 1.0F, 0.0F, false);
        structure.texOffs(0, 0).addBox(-1.0F, -9.001F, -2.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-3.5F, -12.7461F, 0.3174F);
        structure.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.3927F, 0.0F, 0.0F);
        cube_r1.texOffs(0, 0).addBox(-0.5F, -13.5F, -0.5F, 1.0F, 27.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(0, 0).addBox(6.5F, -13.5F, -0.5F, 1.0F, 27.0F, 1.0F, 0.0F, true);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(0.0F, -7.7782F, 4.5F);
        structure.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.7854F);
        cube_r2.texOffs(0, 0).addBox(-0.5F, -5.5F, -0.5F, 1.0F, 11.0F, 1.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(0.0F, -7.7782F, 4.5F);
        structure.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, 0.7854F);
        cube_r3.texOffs(0, 0).addBox(-0.5F, -5.5F, -0.5F, 1.0F, 11.0F, 1.0F, 0.0F, false);

        slate = new ModelRenderer(this);
        slate.setPos(-8.0F, 16.0F, 8.0F);


        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(8.0F, -8.3624F, -7.2671F);
        slate.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.3927F, 0.0F, 0.0F);
        cube_r4.texOffs(0, 0).addBox(-8.0F, -8.0F, -0.5F, 16.0F, 16.0F, 1.0F, 0.0F, false);

        paper = new ModelRenderer(this);
        paper.setPos(0.0F, 24.0F, 0.0F);


        cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(0.0F, -16.5537F, 0.271F);
        paper.addChild(cube_r5);
        setRotationAngle(cube_r5, -0.3927F, 0.0F, 0.0F);
        cube_r5.texOffs(0, 0).addBox(-8.0F, -8.0F, -0.071F, 16.0F, 16.0F, 0.0F, 0.0F, false);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) { }

}