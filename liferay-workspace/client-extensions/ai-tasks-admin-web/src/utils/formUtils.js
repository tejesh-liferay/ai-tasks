/**
 * @author Petteri Karttunen
 */

export const enforceMinMax = (event) => {
  const element = event.currentTarget;

  if (element.value != '') {
    if (element.min && parseInt(element.value) < parseInt(element.min)) {
      element.value = element.min;
    }
    if (element.max && parseInt(element.value) > parseInt(element.max)) {
      element.value = element.max;
    }
  }
};
