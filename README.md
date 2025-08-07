# Compose Onboarding Tour 🚀

This repository contains the full source code for our Medium article: **[Medium Link]([https://www.google.com/search?q=link-to-your-medium-article](https://medium.com/@amartyasingh2002/jetpack-compose-building-a-guided-walkthrough-for-user-onboarding-from-scratch-f5abf4f700be))**.

This project demonstrates how to build a simple, lightweight, and customizable library for creating guided product tours in Jetpack Compose. It's designed to help readers of the article understand the core concepts and see the final, working implementation.

## About This Project

The goal of this project is to provide a clear, hands-on example of how to guide your users, improve feature discovery, and enhance the onboarding experience in a modern Android app.

### Features

  - **Easy to Understand:** The code is broken down into logical components.
  - **State-Driven Logic:** Managed by a `TourState` holder for predictable and testable behavior.
  - **Customizable UI:** The appearance of the overlay and tooltips can be easily modified.
  - **Highlights Any Composable:** The tour can target any UI element, from a simple `Button` to a complex custom view.

## Code Structure

The implementation is split into three main files, each with a distinct responsibility:

### 🧠 `TourState.kt`

This is the **brain** of the system. It's a plain state holder class that has no knowledge of the UI.

  - Manages the list of tour steps (`TourStep`).
  - Tracks the `currentStepIndex` of the tour.
  - Stores the screen coordinates of UI elements in a map (`targetCoordinates`).
  - Provides functions to control the tour's lifecycle (`start()`, `next()`, `dismiss()`).

### 🎨 `TourHost.kt`

This is the **UI layer**. It's responsible for drawing the tour elements on top of your screen.

  - The `TourHost` composable wraps your screen's content.
  - It observes `TourState` to know when to display the tour.
  - It contains the `HighlightOverlay` composable, which draws the dim background and the "hole" for the highlighted view.
  - It also contains the `TooltipPopup` and `Tooltip` composables, which display the description for the current step.
  - The `.tourTarget()` modifier is also defined here, which is used to register a composable's position with the `TourState`.

### 📐 `TooltipPopupPositionProvider.kt`

This is a focused **helper class** that handles positioning logic.

  - Its only job is to calculate the precise X and Y coordinates to place the tooltip.
  - It intelligently decides whether to place the tooltip above or below the target based on available screen space.

## Customization

Feel free to fork this repository and experiment\! You can easily customize the tour's appearance:

  - **Highlight Padding:** Change the `targetPadding` value inside the `TourState` class.
  - **Overlay Color:** Modify the `scrimColor` in the `HighlightOverlay` composable (`TourHost.kt`).
  - **Tooltip UI:** The `Tooltip` composable is a standard Material `Card`. Change its shape, elevation, colors, and layout to match your app's design system.
  - **Animations:** Add animations to the appearance of the overlay or tooltip for a more polished feel.
