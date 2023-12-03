public class NumberGenerator {

        private static int generatedId;
    
        public static int generateId() {
            generatedId = (int) (Math.random() * 10000);
            return generatedId;
        }
    
        public static int getGeneratedId() {
            return generatedId;
        }
    }