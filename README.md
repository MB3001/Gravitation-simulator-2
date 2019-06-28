# Gravitation-simulator-2
Gravitation simulator where bodies are all attracted to each other.

My second game made in this game engine.

You can move with 'ASDW' and shoot balls with left click. You close the program with "Esc" key.

This will make life easier for those who want to create games set in space, astronomical and scientific simulations, teach physics by giving the program educational applications or simply have a good time playing and improving it through programming in JMonkeyEngine.

You can play it directly by opening "MyGame.jar" in the "dist" folder.

It is built with JMonkeyEngine (http://jmonkeyengine.org/).

In the line 36 of Main.java are the physical variables, if you are interested in changing them.
     
    /**
     * Prepare physics.
     */
    float gravitational_constant = 10f;
    float canonball_speed = 2f;
    float canonball_mass = 1f;

If you want to know directly what is the block of code that does the magic of gravitation, I'll throw the spoiler at you:

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


Author: Matías Bonino
