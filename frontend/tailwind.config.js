/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#536600',
        'primary-container': '#caf300',
        surface: '#f9f9f9',
        'surface-container': '#ebeeef',
        'surface-container-low': '#ffffff',
        'surface-container-high': '#e4e9ea',
        'on-surface': '#2d3435',
        error: '#9f403d',
      },
      fontFamily: {
        heading: ['Manrope', 'sans-serif'],
        body: ['Inter', 'sans-serif'],
      },
      fontSize: {
        'display-lg': ['3.5rem', { lineHeight: '1.2', letterSpacing: '-0.02em' }],
        'headline-lg': ['2rem', { lineHeight: '1.3', fontWeight: '600' }],
      },
    },
  },
  plugins: [],
}