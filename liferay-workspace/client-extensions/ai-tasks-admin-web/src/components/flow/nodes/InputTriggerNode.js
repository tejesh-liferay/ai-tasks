/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React, { memo } from 'react';

import BaseNode from './BaseNode';

const InputTriggerNode = memo(({ data, id, type }) => {
  const { label, parameters } = data;

  return (
    <BaseNode className="trigger-node" hasTargetHandle="false" id={id} label={label} type={type} />
  );
});

export default InputTriggerNode;
