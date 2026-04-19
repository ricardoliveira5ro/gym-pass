/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#a3c621',
        'primary-container': '#3d4a00',
        surface: '#121212',
        'surface-container': '#1e1e1e',
        'surface-container-low': '#252525',
        'surface-container-high': '#2d2d2d',
        'on-surface': '#e0e0e0',
        'on-surface/70': 'rgba(224, 224, 224, 0.7)',
        error: '#ea6b69',
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