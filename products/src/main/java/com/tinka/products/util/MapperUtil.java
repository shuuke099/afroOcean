package com.tinka.products.util;

import java.text.Normalizer;
import java.util.Locale;

public class MapperUtil {

    // 🔤 Generate a slug from title or name
    public static String toSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String nowhitespace = input.trim().replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        return normalized.replaceAll("[^\\w-]", "").toLowerCase(Locale.ENGLISH);
    }

    // 📦 You can add more static mapping helpers here in future
}
