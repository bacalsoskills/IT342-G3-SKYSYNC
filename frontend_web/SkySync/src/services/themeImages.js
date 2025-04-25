// Import the available images
import WhiteTShirt from "../wardrobe_pictures/cloudy_comfort_1.jpeg"; // Correct path
import BlackLongSleeve from "../wardrobe_pictures/layered_look_1.jpeg"; // Correct path
import LightSweater from "../wardrobe_pictures/cloudy_comfort_2.jpeg"; // Correct path
import Hoodie from "../wardrobe_pictures/layered_look_2.jpg"; // Correct path
import ShortSleeve from "../wardrobe_pictures/sunny_day_essentials_1.jpeg"; // Correct path
import LinenTop from "../wardrobe_pictures/sunny_day_essentials_2.jpeg"; // Correct path
import SwimSuit from "../wardrobe_pictures/beachwear_1.jpeg"; // Correct path
import FlipFlop from "../wardrobe_pictures/beachwear_2.jpg"; // Correct path
import BreathableTop from "../wardrobe_pictures/casual_&_everyday_wear_1.jpg"; // Correct path
import Chinos from "../wardrobe_pictures/casual_&_everyday_wear_2.jpeg"; // Correct path
import ButtonUp from "../wardrobe_pictures/smart_casual_1.jpg"; // Correct path
import TailoredShirt from "../wardrobe_pictures/smart_casual_2.jpg"; // Correct path
import ReflectiveJacket from "../wardrobe_pictures/foggy_morning_attire_1.jpg"; // Correct path
import LongPants from "../wardrobe_pictures/foggy_morning_attire_2.jpg"; // Correct path
import ColorfulJacket from "../wardrobe_pictures/visibility_gear_1.jpg"; // Correct path
import WaterProofBoots from "../wardrobe_pictures/visibility_gear_2.jpeg"; // Correct path
import WaterProofJacket from "../wardrobe_pictures/rainy_day_protection_1.jpeg"; // Correct path
import QuickDryPants from "../wardrobe_pictures/rainy_day_protection_2.jpeg"; // Correct path
import TrenchCoat from "../wardrobe_pictures/urban_rainwear_1.jpg"; // Correct path
import LargeUmbrella from "../wardrobe_pictures/urban_rainwear_2.jpeg"; // Correct path

// names BreathableTop
// Map themes to their respective images
const themeImages = {
  "Sunny Day Essentials": [ShortSleeve, LinenTop], // Add images for this theme
  "Beachwear": [SwimSuit, FlipFlop], // Add images for this theme
  "Casual & Everyday Wear": [BreathableTop, Chinos], // Add images for this theme
  "Smart Casual": [ButtonUp, TailoredShirt], // Add images for this theme
  "Cloudy Comfort": [WhiteTShirt, LightSweater], // Existing theme
  "Layered Look": [BlackLongSleeve, Hoodie], // Existing theme
  "Foggy Morning Attire": [ReflectiveJacket, LongPants], // Add images for this theme
  "Visibility Gear": [ColorfulJacket, WaterProofBoots], // Add images for this theme
  "Rainy Day Protection": [WaterProofJacket, QuickDryPants], // Add images for this theme
  "Urban Rainwear": [TrenchCoat, LargeUmbrella], // Add images for this theme
  "Winter Warmth": [], // Add images for this theme
  "Snow Adventure Gear": [], // Add images for this theme
  "Versatile Everyday Wear": [], // Add images for this theme
  "Comfortable Basics": [] // Add images for this theme
};

export default themeImages;