package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import static com.jme3.math.Vector3f.ZERO;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author Matías Bonino
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    private BulletAppState bulletAppState;

    /**
     * Prepare physics.
     */
    float velocity = 2f;
    float gravitational_constant = 10f;
    float canonball_mass = 1;

    /**
     * Prepare HUD.
     */
    BitmapText hudText;
    private long t = 0;
    private int quantity = 0;

    private RigidBodyControl ball_phy;
    private static Sphere sphere;

    static {
        /**
         * Initialize the cannon ball geometry
         */
        sphere = new Sphere(32, 32, 0.4f, true, false);
        sphere.setTextureMode(TextureMode.Projected);
    }

    @Override
    public void simpleInitApp() {
        /**
         * Set up Physics Game
         */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        flyCam.setMoveSpeed(30 * speed);

        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "shoot");

        Box b = new Box(1, 1, 1);
        Geometry b_geom = new Geometry("Box", b);
        Material b_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        b_mat.setColor("Color", ColorRGBA.Blue);
        b_geom.setMaterial(b_mat);
        rootNode.attachChild(b_geom);
    }

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("shoot") && !keyPressed) {
                makeCannonBall();
            }
        }
    };

    public void makeCannonBall() {

        Geometry ball_geo = new Geometry("cannon ball", sphere);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Gray);
        ball_geo.setMaterial(mat);
        rootNode.attachChild(ball_geo);

        ball_geo.setLocalTranslation(cam.getLocation());
        ball_phy = new RigidBodyControl(canonball_mass);

        ball_geo.addControl(ball_phy);
        bulletAppState.getPhysicsSpace().add(ball_phy);
        ball_phy.setGravity(ZERO);

        quantity++;

        ball_phy.setLinearVelocity(cam.getDirection().mult(velocity));
    }

    @Override
    public void simpleUpdate(float tpf) {

        for (PhysicsRigidBody attractor : bulletAppState.getPhysicsSpace().getRigidBodyList()) {

            for (PhysicsRigidBody attracted : bulletAppState.getPhysicsSpace().getRigidBodyList()) {
                if (attractor != attracted) {
                    attracted.applyCentralForce(
                            (attractor.getPhysicsLocation().subtract(attracted.getPhysicsLocation()))
                                    .normalize().mult(gravitational_constant * attractor.getMass() * attracted.getMass()
                                            / attracted.getPhysicsLocation().distanceSquared(attractor.getPhysicsLocation())));
                }
            }
        }

        if (t % 60 == 0) {
            if (hudText != null) {
                guiNode.detachChild(hudText);
            }
            hudText = new BitmapText(guiFont, false);
            hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
            hudText.setColor(ColorRGBA.Cyan);                             // font color
            hudText.setText(
                    // the text
                    "Position: " + cam.getLocation()
                    + "\nCamera direction: " + cam.getDirection()
                    + "\nAmount of cannonballs created: " + quantity
                    + "\nGravitational constant: " + gravitational_constant
                    + "\nCannon ball initial velocity: " + velocity
                    + "\nCannon ball mass: " + canonball_mass
            );
            hudText.setLocalTranslation(300, hudText.getLineHeight() * 6, 0); // position
            guiNode.attachChild(hudText);
        }

        t++;
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
