//package main.entities;
//
//import main.entities.pickup.Sword;
//import main.math.Vec2i;
//
//public class SwordWielderComponent {
//
//    private Avatar avatar;
//    private Sword sword;
//
//    public void swingSword() {
//        // cannot swing if has no sword
//        if (sword == null) return;
//
//        // kill the entity in the avatar's direction
//        Vec2i target = pos.add(direction);
//        if (level.isValidGridPos(target) && level.hasEnemy(target)) {
//
//            // Kill the enemy
//            level.getEnemy(target).onDestroyed();
//            sword.reduceDurability();
//
//            // check durability and destroy
//            if (sword.isBroken())
//                onSwordUnequipped();
//
//            endTurn();
//        }
//    }
//
//    /**
//     * when the player picks up a sword
//     * @param s
//     */
//    public void onSwordEquipped(Sword s) {
//        sword = s;
//        swordView.setVisible(true);
//    }
//
//    /**
//     * when the player loses their sword
//     */
//    public void onSwordUnequipped() {
//        sword = null;
//        swordView.setVisible(false);
//    }
//}
