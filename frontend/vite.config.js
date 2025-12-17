// javascript
import path from "path";
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  // set this to the folder containing your .env (replace '../backend' with actual path)
  envDir: path.resolve(__dirname, "../backend"),

  plugins: [react()],

  optimizeDeps: {
    // ensure these get pre-bundled to avoid mixed import/runtime issues
    include: ["react", "react-dom", "lucide-react"]
  },

  resolve: {
    alias: {
      // force all imports to use the single react in this project's node_modules
      react: path.resolve(__dirname, "node_modules/react"),
      "react-dom": path.resolve(__dirname, "node_modules/react-dom")
    }
  }
});
