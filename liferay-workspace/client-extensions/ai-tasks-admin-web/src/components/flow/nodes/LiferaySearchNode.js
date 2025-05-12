/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React, { memo } from 'react';

import BaseNode from './BaseNode';

const LiferaySearchNode = memo(({ data, id, type }) => {
  const { label, parameters } = data;
  const details = [
    ['blueprint', parameters.sxpBlueprintExternalReferenceCode],
    ['output', parameters.taskContextOutputParameterName],
  ];

  return <BaseNode className="utility-node" details={details} id={id} label={label} type={type} />;
});

export default LiferaySearchNode;
