package edu.cit.Skysync.service;

import org.springframework.stereotype.Service;

import edu.cit.Skysync.dto.DailyWeatherDTO;

@Service
public class WardrobeService {
    
    public WardrobeRecommendation getOutfitRecommendation(int weatherCode) {
        return switch (weatherCode) {
            // Clear or mainly clear weather
            case 0, 1 -> new WardrobeRecommendation(
                "Sunny Day Essentials",
                new String[] {
                    "Lightweight short-sleeve shirt",
                    "Breathable cotton or linen top",
                    "Comfortable shorts or skirt",
                    "UV-protection sunglasses",
                    "Wide-brimmed hat"
                },
                new String[] {
                    "Choose light colors to reflect sunlight",
                    "Natural fabrics help with breathability",
                    "Keep cool with airy bottoms",
                    "Protect your eyes from UV rays",
                    "Extra protection for face and neck"
                }
            );

            // Partly cloudy
            case 2 -> new WardrobeRecommendation(
                "Casual & Everyday Wear",
                new String[] {
                    "Short-sleeve shirt or breathable top",
                    "Chinos or comfortable shorts",
                    "Loafers or casual slip-ons",
                    "Denim jacket or windbreaker"
                },
                new String[] {
                    "Ideal if the temperature is mild",
                    "Keeps it casual while allowing flexibility",
                    "A stylish alternative to sneakers",
                    "Light enough for comfort but adds a stylish layer"
                }
            );

            // Overcast
            case 3 -> new WardrobeRecommendation(
                "Cloudy Comfort",
                new String[] {
                    "Long-sleeve lightweight shirt",
                    "Light sweater or cardigan",
                    "Comfortable jeans or trousers",
                    "Water-resistant shoes"
                },
                new String[] {
                    "Provides light warmth without overheating",
                    "Easy to remove if sun comes out",
                    "Versatile for changing conditions",
                    "Protection against potential drizzle"
                }
            );

            // Foggy conditions
            case 45, 48 -> new WardrobeRecommendation(
                "Foggy Morning Attire",
                new String[] {
                    "Light jacket with reflective elements",
                    "Long pants for protection",
                    "Comfortable waterproof shoes",
                    "Beanie or light scarf"
                },
                new String[] {
                    "Visibility and warmth in foggy conditions",
                    "Keeps legs protected from moisture",
                    "Keeps feet dry in damp conditions",
                    "Light head/neck coverage"
                }
            );

            // Rainy weather
            case 51, 53, 55, 61, 63, 65, 80, 81, 82 -> new WardrobeRecommendation(
                "Rainy Day Protection",
                new String[] {
                    "Waterproof jacket with hood",
                    "Quick-dry pants or rain pants",
                    "Waterproof boots or shoes",
                    "Compact umbrella"
                },
                new String[] {
                    "Essential for staying dry in rain",
                    "Avoids soaked clothing all day",
                    "Keeps feet dry in puddles",
                    "Extra protection when needed"
                }
            );

            // Snowy weather
            case 71, 73, 75 -> new WardrobeRecommendation(
                "Winter Warmth",
                new String[] {
                    "Insulated winter coat",
                    "Thermal base layers",
                    "Waterproof snow boots",
                    "Warm gloves and hat"
                },
                new String[] {
                    "Windproof and water-resistant essential",
                    "Wicks moisture while retaining heat",
                    "Traction and warmth for snowy paths",
                    "Protects extremities from cold"
                }
            );

            // Default recommendation
            default -> new WardrobeRecommendation(
                "Versatile Everyday Wear",
                new String[] {
                    "Comfortable t-shirt or blouse",
                    "Light jacket or sweater",
                    "Versatile jeans or slacks",
                    "Comfortable walking shoes"
                },
                new String[] {
                    "Easy to layer if weather changes",
                    "Can be removed if it gets warmer",
                    "Works for most casual situations",
                    "Suitable for various conditions"
                }
            );
        };
    }

    public WardrobeRecommendation getTodayOutfitRecommendation(DailyWeatherDTO weather) {
        return getOutfitRecommendation(weather.getWeatherCode());
    }

    public static class WardrobeRecommendation {
        private String theme;
        private String[] clothingItems;
        private String[] clothingDescriptions;

        public WardrobeRecommendation(String theme, String[] clothingItems, String[] clothingDescriptions) {
            this.theme = theme;
            this.clothingItems = clothingItems;
            this.clothingDescriptions = clothingDescriptions;
        }

        // Getters
        public String getTheme() {
            return theme;
        }

        public String[] getClothingItems() {
            return clothingItems;
        }

        public String[] getClothingDescriptions() {
            return clothingDescriptions;
        }
    }
}