/**
 * @author Petteri Karttunen
 */
import React, { memo } from 'react';

import BaseNode from './BaseNode';

const GoogleImagenNode = memo(({ data, id, type }) => {
  const { label, parameters } = data;
  const details = [
    ['modelName', parameters.modelName],
    ['responseFormat', parameters.responseFormat],
  ];

  return <BaseNode className="image-node" details={details} id={id} label={label} type={type} />;
});

export default GoogleImagenNode;
