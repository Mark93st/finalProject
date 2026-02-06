/** @type {import('tailwindcss').Config} */
module.exports = {
  // 1. Tell Tailwind where to look for class names
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/**/*.js"
  ],
  theme: {
    extend: {
      // 2. Extend the default palette here if you need custom brand colors
      colors: {
        'brand-blue': '#1e40af', 
      },
    },
  },
  plugins: [
    // 3. Useful plugins for standard HTML elements (requires installation)
    require('@tailwindcss/forms'),
    // require('@tailwindcss/typography'), 
  ],
}